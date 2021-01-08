from openpyxl import load_workbook

wb = load_workbook(filename = "data.xlsx")

ws = wb.active
sheet_ranges = wb['Onlineprodukt_Gemeinden']
print(sheet_ranges['H24'].value)

f = open("cities.csv", "w")

for i in range(16076):
    print("----------------------------------")
    print("number: " + str(i))
    name = sheet_ranges[str("H" + str(i+1))].value
    longitude = sheet_ranges[str("O" + str(i+1))].value
    latitude = sheet_ranges[str("P" + str(i+1))].value
    key = sheet_ranges[str("G" + str(i+1))].value
    print("Name: " + str(name))
    print("Longitude: " + str(longitude))
    print("Latitude: " +  str(latitude))
    if str(name) != "None" and str(longitude) != "None":
        f.write(str(name) + ";" + str(latitude).replace(",", ".") + ";" + str(longitude).replace(",", ".") + "\n")

f.close()
