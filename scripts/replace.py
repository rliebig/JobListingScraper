
file = open('test.html', 'r')

text = file.read()
text = text.replace("L.marker", "try { L.marker").replace(";", ";} catch(err) { console.log(err); }")
newfile = open("output.html", "w")
newfile.write(text)
