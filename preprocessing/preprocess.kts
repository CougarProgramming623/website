import java.io.File



fun process(lines: List<String>,indent: String):List<String> {
    var currentIndent = "";
    val x = lines.flatMap {
        val trimmed = it.trim()
        if(trimmed.startsWith("[[") && trimmed.endsWith("]]")) {
            process(File(trimmed.substring(2,trimmed.length - 2)).readLines(Charsets.UTF_8),currentIndent)
        } else {
            currentIndent = it.substring(0,it.indexOfFirst { it != ' ' }.takeUnless { it == -1 } ?: 0)
            listOf(it)
        }
    }.map { indent + it }
    return x
}

fun preprocess(file: File) { // starts with .htmlx
    check(file.name.endsWith(".htmlx")) { "file $file doesn't end with .htmlx ending" }
    val text = process(file.readLines(Charsets.UTF_8),"").fold("") { a, b -> "$a\n$b"}
    File(file.name.substring(0,file.name.length - 1)).writeText(text,Charsets.UTF_8)
}

for(file in File("./").listFiles()) {
    if(file.name.endsWith(".htmlx")) {
        println("processing $file")
        preprocess(file)
    }
}