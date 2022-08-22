package com.example.myapplication.android

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.DatabaseDriverFactory
import com.example.myapplication.Greeting
import com.example.myapplication.cache.Hello

import java.util.*

class MainActivity : AppCompatActivity(), SelectedItem {

    private var greeting: Greeting? = null
    var listAdapter: ListAdapter? = null
    var dataList : ArrayList<Hello> = ArrayList()
    var edtName: EditText? = null
    var txtDate: TextView? = null
    var selectProject : String = "Select Project"
    var spinnerArray : Array<String>? = null
    var spnProject: Spinner? = null
    var isUpdate : Boolean = false
    var updatePos : Int = 0;

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        greeting = Greeting(DatabaseDriverFactory(applicationContext))

        spinnerArray = resources.getStringArray(R.array.project_array)

        val dateLout: LinearLayout = findViewById(R.id.dateLout)
        txtDate= findViewById(R.id.txtDate)
        edtName = findViewById(R.id.edtName)

        spnProject = findViewById(R.id.spnProject)
        val btnAddData: Button  = findViewById(R.id.btnAddData)
        val rvList: RecyclerView  = findViewById(R.id.rvList)

        var cal = Calendar.getInstance()

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "dd MMM, yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                txtDate!!.text = sdf.format(cal.time)
            }
        }

        dateLout.setOnClickListener (object: View.OnClickListener{
            override fun onClick(p0: View?) {
                DatePickerDialog(this@MainActivity, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        })

        spnProject!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectProject = parent.getItemAtPosition(position).toString()
            }

        }

        btnAddData.setOnClickListener (object: View.OnClickListener{
            override fun onClick(p0: View?) {
                if(txtDate!!.text.toString() == ""){
                    Toast.makeText(applicationContext, "Please select date", Toast.LENGTH_SHORT).show()
                } else if(edtName!!.text.toString().trim() == ""){
                    Toast.makeText(applicationContext, "Please enter name", Toast.LENGTH_SHORT).show()
                } else if(selectProject == "Select Project"){
                    Toast.makeText(applicationContext, "Please select project", Toast.LENGTH_SHORT).show()
                } else {
                    if(isUpdate){
                        greeting!!.updateData(
                            dataList[updatePos].id,
                            txtDate!!.text.toString(),
                            edtName!!.text.toString().trim(),
                            selectProject
                        )
                    } else {
                        greeting!!.insertData(
                            txtDate!!.text.toString(),
                            edtName!!.text.toString().trim(),
                            selectProject
                        )
                    }
                    updatePos = 0
                    isUpdate = false
                    edtName!!.setText("")
                    txtDate!!.text = ""
                    selectProject = ""
                    spnProject!!.setSelection(0)
                    dataList.clear()
                    dataList.addAll(greeting!!.getData() as ArrayList<Hello>)
                    listAdapter!!.notifyDataSetChanged()
                }
            }
        })

        dataList = greeting!!.getData() as ArrayList<Hello>
        listAdapter = ListAdapter(dataList,this)
        val layoutManager = LinearLayoutManager(applicationContext)
        rvList.layoutManager = layoutManager
        rvList.itemAnimator = DefaultItemAnimator()
        rvList.adapter = listAdapter

    }

    override fun edit(pos: Int) {
        updatePos = pos
        isUpdate = true
        edtName!!.setText(dataList[pos].name)
        txtDate!!.text = dataList[pos].date
        for (i in spinnerArray!!.indices){
            if(spinnerArray!![i] == dataList[pos].project){
                spnProject!!.setSelection(i)
                selectProject = dataList[pos].project
                break
            }
        }
    }

    override fun delete(pos: Int) {
        greeting!!.deleteData(dataList[pos].id)
        dataList.removeAt(pos)
        listAdapter!!.notifyDataSetChanged()
    }

}

interface SelectedItem {
    fun edit(pos: Int)
    fun delete(pos: Int)
}
