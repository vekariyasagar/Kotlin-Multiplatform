import SwiftUI
import shared

struct ContentView: View {

    var greeting = Greeting(databaseDriverFactory: DatabaseDriverFactory())
    
    @State var name: String = ""
    
    @State private var selectedDateText: String = ""
    
    @State var dataList : [Hello] = []
    @State var updateId : Int64 = 0
    @State var isUpdate = false
    
    @State var selectedDate = Date()

    private func setDateString() {
        let formatter = DateFormatter()
        formatter.dateFormat = "dd MMM, yyyy"
        self.selectedDateText = formatter.string(from: self.selectedDate)
        print(self.selectedDateText)
    }

    @State var value = ""
    var placeholder = "Select Project"
    var dropDownList = ["Design", "Development", "Testing", "Deployment"]

	var body: some View {
        
        GeometryReader { gr in
            let leftWidth = gr.size.width * 0.35
            let rightWidth = gr.size.width * 0.50
            VStack(spacing : 15){
                HStack{
                    Text("Select Date")
                        .frame(width: leftWidth, alignment: .topLeading)
                        .font(Font.headline.weight(.bold))
                    DatePicker("", selection: $selectedDate, displayedComponents: .date)
                        .labelsHidden()
                        .frame(width: rightWidth, height: 55, alignment: .leading)
                        .padding(.horizontal)
                        .overlay(RoundedRectangle(cornerRadius: 10).stroke(Color.blue, lineWidth: 3)
                        .onChange(of: selectedDate, perform: { value in
                            self.setDateString()
                        })
                    )
                }
                HStack{
                    Text("Enter Name")
                        .frame(width: leftWidth, alignment: .topLeading)
                        .font(Font.headline.weight(.bold))
                    TextField("",text: $name)
                        .frame(width: rightWidth, height: 20, alignment: .topLeading)
                        .padding()
                        .overlay(RoundedRectangle(cornerRadius: 10).stroke(Color.blue, lineWidth: 3)
                    )
                }
                HStack{
                    Text("Select Project")
                        .frame(width: leftWidth,alignment: .topLeading)
                        .font(Font.headline.weight(.bold))
                    Menu {
                        ForEach(dropDownList, id: \.self){ client in
                            Button(client) {
                                self.value = client
                            }
                        }
                    } label: {
                        VStack(spacing: 5){
                            HStack{
                                Text(value.isEmpty ? placeholder : value)
                                    .foregroundColor(value.isEmpty ? .gray : .black)
                                Spacer()
                                Image(systemName: "chevron.down")
                                    .foregroundColor(Color.accentColor)
                                    .font(Font.system(size: 15, weight: .bold))
                            }
                        }
                    }
                    .frame(width: rightWidth, alignment: .topLeading)
                    .padding()
                    .overlay(RoundedRectangle(cornerRadius: 10).stroke(Color.blue, lineWidth: 3))
                
                }
                Button(action: {
                    print("Click on Add Data Button")
                    if(name == ""){
                        
                    } else if(value == ""){
                        
                    } else {
                        if(isUpdate){
                            greeting.updateData(id: updateId, date: selectedDateText, name: name, project: value)
                        }else{
                            greeting.insertData(date: selectedDateText, name: name, project: value)
                        }
                        updateId = 0
                        isUpdate = false
                        dataList = greeting.getData()
                        name = ""
                        value = ""
                        selectedDate = Date()
                    }
                  
                }) {
                    Text("Add Data")
                        .frame(width: 100)
                        .font(Font.headline.weight(.bold))
                }
                .padding()
                .foregroundColor(.white)
                .background(Color.accentColor)
                .cornerRadius(10)
                List(dataList , id: \.id) { data in
                        HStack(spacing : 15){
                            VStack(spacing: 5){
                                Text("Date : \(data.date)")
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                    .font(Font.headline.weight(.regular))
                                Text("Name : \(data.name)")
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                    .font(Font.headline.weight(.regular))
                                Text("Project : \(data.project)")
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                    .font(Font.headline.weight(.regular))
                            }
                            Image("ic_edit")
                                .resizable()
                                .frame(width: 32, height: 32)
                                .foregroundColor(Color.accentColor)
                                .onTapGesture {
                                    updateId = data.id
                                    isUpdate = true
                                    name = data.name
                                    value = data.project
                                    
                                    let dateFormatter = DateFormatter()
                                    dateFormatter.dateFormat = "dd MMM, yyyy"
                                    let date = dateFormatter.date(from: data.date)!
                                    selectedDate = date
                                    print(selectedDate)
                                }
                            Image("ic_delete")
                                .resizable()
                                .frame(width: 30, height: 30)
                                .foregroundColor(Color.accentColor)
                                .onTapGesture {
                                    greeting.deleteData(id: data.id)
                                    dataList = greeting.getData()
                                }
                        }
                }.listStyle(PlainListStyle())
            }
        }.onAppear(
            perform: {
                self.setDateString()
                dataList = greeting.getData()
            }
        )
    }
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}

