@file:Suppress("ConvertCallChainIntoSequence")

package lesson6.task1

import lesson2.task2.daysInMonth
import kotlin.math.floor

// Урок 6: разбор строк, исключения
// Максимальное количество баллов = 13
// Рекомендуемое количество баллов = 11
// Вместе с предыдущими уроками (пять лучших, 2-6) = 40/54

/**
 * Пример
 *
 * Время представлено строкой вида "11:34:45", содержащей часы, минуты и секунды, разделённые двоеточием.
 * Разобрать эту строку и рассчитать количество секунд, прошедшее с начала дня.
 */
fun timeStrToSeconds(str: String): Int {
    val parts = str.split(":")
    var result = 0
    for (part in parts) {
        val number = part.toInt()
        result = result * 60 + number
    }
    return result
}

/**
 * Пример
 *
 * Дано число n от 0 до 99.
 * Вернуть его же в виде двухсимвольной строки, от "00" до "99"
 */
fun twoDigitStr(n: Int) = if (n in 0..9) "0$n" else "$n"

/**
 * Пример
 *
 * Дано seconds -- время в секундах, прошедшее с начала дня.
 * Вернуть текущее время в виде строки в формате "ЧЧ:ММ:СС".
 */
fun timeSecondsToStr(seconds: Int): String {
    val hour = seconds / 3600
    val minute = (seconds % 3600) / 60
    val second = seconds % 60
    return String.format("%02d:%02d:%02d", hour, minute, second)
}

/**
 * Пример: консольный ввод
 */
fun main() {
    println("Введите время в формате ЧЧ:ММ:СС")
    val line = readLine()
    if (line != null) {
        val seconds = timeStrToSeconds(line)
        if (seconds == -1) {
            println("Введённая строка $line не соответствует формату ЧЧ:ММ:СС")
        } else {
            println("Прошло секунд с начала суток: $seconds")
        }
    } else {
        println("Достигнут <конец файла> в процессе чтения строки. Программа прервана")
    }
}


/**
 * Средняя (4 балла)
 *
 * Дата представлена строкой вида "15 июля 2016".
 * Перевести её в цифровой формат "15.07.2016".
 * День и месяц всегда представлять двумя цифрами, например: 03.04.2011.
 * При неверном формате входной строки вернуть пустую строку.
 *
 * Обратите внимание: некорректная с точки зрения календаря дата (например, 30.02.2009) считается неверными
 * входными данными.
 */
fun dateStrToDigit(str: String): String {
    val parts = str.split(" ")
    val day: Int
    val month: Int
    val year: Int
    try {
        day = parts[0].toInt()
        year = parts[2].toInt()
        month = when (parts[1]) {
            "января" -> 1
            "февраля" -> 2
            "марта" -> 3
            "апреля" -> 4
            "мая" -> 5
            "июня" -> 6
            "июля" -> 7
            "августа" -> 8
            "сентября" -> 9
            "октября" -> 10
            "ноября" -> 11
            "декабря" -> 12
            else -> 0
        }
    } catch (e: NumberFormatException) {
        return ""
    }
    if (month == 0 || day !in 1..daysInMonth(month, year) || year < 0) return ""
    return String.format("%02d.%02d.%d", day, month, year)
}

/**
 * Средняя (4 балла)
 *
 * Дата представлена строкой вида "15.07.2016".
 * Перевести её в строковый формат вида "15 июля 2016".
 * При неверном формате входной строки вернуть пустую строку
 *
 * Обратите внимание: некорректная с точки зрения календаря дата (например, 30 февраля 2009) считается неверными
 * входными данными.
 */
fun dateDigitToStr(digital: String): String {
    val parts = digital.split(".")
    val day: Int
    val month: Int
    val year: Int
    try {
        day = parts[0].toInt()
        month = parts[1].toInt()
        year = parts[2].toInt()
    } catch (e: NumberFormatException) {
        return ""
    }
    if (month !in 1..12 || day !in 1..daysInMonth(month, year) || year < 0 || parts.size != 3) return ""
    val monthStr = when (month) {
        1 -> "января"
        2 -> "февраля"
        3 -> "марта"
        4 -> "апреля"
        5 -> "мая"
        6 -> "июня"
        7 -> "июля"
        8 -> "августа"
        9 -> "сентября"
        10 -> "октября"
        11 -> "ноября"
        12 -> "декабря"
        else -> "ошибки"
    }
    return "$day $monthStr $year"
}

/**
 * Средняя (4 балла)
 *
 * Номер телефона задан строкой вида "+7 (921) 123-45-67".
 * Префикс (+7) может отсутствовать, код города (в скобках) также может отсутствовать.
 * Может присутствовать неограниченное количество пробелов и чёрточек,
 * например, номер 12 --  34- 5 -- 67 -89 тоже следует считать легальным.
 * Перевести номер в формат без скобок, пробелов и чёрточек (но с +), например,
 * "+79211234567" или "123456789" для приведённых примеров.
 * Все символы в номере, кроме цифр, пробелов и +-(), считать недопустимыми.
 * При неверном формате вернуть пустую строку.
 *
 * PS: Дополнительные примеры работы функции можно посмотреть в соответствующих тестах.
 */
fun flattenPhoneNumber(phone: String): String {
    if (phone.contains("""(.*\(\).*)|[^0-9+()\- ]+""".toRegex()))
        return ""
    if (!phone.contains("""\d+""".toRegex()))
        return ""
    return phone.replace("""\)|\(|-|\s""".toRegex(), "")
}

/**
 * Средняя (5 баллов)
 *
 * Результаты спортсмена на соревнованиях в прыжках в длину представлены строкой вида
 * "706 - % 717 % 703".
 * В строке могут присутствовать числа, черточки - и знаки процента %, разделённые пробелами;
 * число соответствует удачному прыжку, - пропущенной попытке, % заступу.
 * Прочитать строку и вернуть максимальное присутствующее в ней число (717 в примере).
 * При нарушении формата входной строки или при отсутствии в ней чисел, вернуть -1.
 */
fun bestLongJump(jumps: String): Int {
    if (jumps.contains("""[^0-9\-% ]+""".toRegex()))
        return -1
    val arr = jumps.replace("""[-%]""".toRegex(), "")
        .replace("""  +""".toRegex(), " ")
        .split(" ")
        .filter { it != "" }
    var best = -1
    for (res in arr) {
        val resNum = res.toInt()
        if (resNum > best)
            best = resNum
    }
    return best
}

/**
 * Сложная (6 баллов)
 *
 * Результаты спортсмена на соревнованиях в прыжках в высоту представлены строкой вида
 * "220 + 224 %+ 228 %- 230 + 232 %%- 234 %".
 * Здесь + соответствует удачной попытке, % неудачной, - пропущенной.
 * Высота и соответствующие ей попытки разделяются пробелом.
 * Прочитать строку и вернуть максимальную взятую высоту (230 в примере).
 * При нарушении формата входной строки, а также в случае отсутствия удачных попыток,
 * вернуть -1.
 */
fun bestHighJump(jumps: String): Int {
    if (jumps.contains("""[^0-9\-%+ ]+""".toRegex()))
        return -1
    val arr = ("$jumps ").replace("""[%-]+""".toRegex(), "")
        .replace("""\d+ {2}""".toRegex(), "")
        .replace(" + ", " ")
        .split(" ")
        .filter { it != "" }
    return arr.last().toInt()
}

/**
 * Сложная (6 баллов)
 *
 * В строке представлено выражение вида "2 + 31 - 40 + 13",
 * использующее целые положительные числа, плюсы и минусы, разделённые пробелами.
 * Наличие двух знаков подряд "13 + + 10" или двух чисел подряд "1 2" не допускается.
 * Вернуть значение выражения (6 для примера).
 * Про нарушении формата входной строки бросить исключение IllegalArgumentException
 */
fun plusMinus(expression: String): Int {
    val arr = expression.split(" ")
    var current: Int
    try {
        if (!arr[0][0].isDigit()) throw IllegalArgumentException()
        current = arr[0].toInt()
        for (i in 1 until arr.size step 2) {
            if (!arr[i + 1][0].isDigit()) throw IllegalArgumentException()
            if (arr[i] == "+")
                current += arr[i + 1].toInt()
            else if (arr[i] == "-")
                current -= arr[i + 1].toInt()
            else
                throw IllegalArgumentException()
        }
    } catch (e: NumberFormatException) {
        throw IllegalArgumentException()
    }
    return current
}

/**
 * Сложная (6 баллов)
 *
 * Строка состоит из набора слов, отделённых друг от друга одним пробелом.
 * Определить, имеются ли в строке повторяющиеся слова, идущие друг за другом.
 * Слова, отличающиеся только регистром, считать совпадающими.
 * Вернуть индекс начала первого повторяющегося слова, или -1, если повторов нет.
 * Пример: "Он пошёл в в школу" => результат 9 (индекс первого 'в')
 */
fun firstDuplicateIndex(str: String): Int {
    var previousWord = Pair("", -1)
    var currentWord = ""
    var indBegin = 0
    for (i in str.indices) {
        if (str[i] == ' ') {
            if (currentWord == previousWord.first)
                return previousWord.second
            else
                previousWord = Pair(currentWord, indBegin)
            currentWord = ""
            indBegin = i + 1
        } else
            currentWord += str[i].lowercase()
    }
    return if (currentWord == previousWord.first) previousWord.second else -1
}

/**
 * Сложная (6 баллов)
 *
 * Строка содержит названия товаров и цены на них в формате вида
 * "Хлеб 39.9; Молоко 62; Курица 184.0; Конфеты 89.9".
 * То есть, название товара отделено от цены пробелом,
 * а цена отделена от названия следующего товара точкой с запятой и пробелом.
 * Вернуть название самого дорогого товара в списке (в примере это Курица),
 * или пустую строку при нарушении формата строки.
 * Все цены должны быть больше нуля либо равны нулю.
 */
fun mostExpensive(description: String): String = try {
    val prices = description.split("; ")
    var best = Pair("", -1.0)
    for (unit in prices) {
        val data = unit.split(" ")
        val price = data[1].toDouble()
        if (price > best.second)
            best = Pair(data[0], price)
    }
    best.first
} catch (e: NumberFormatException) {
    ""
}

/**
 * Сложная (6 баллов)
 *
 * Перевести число roman, заданное в римской системе счисления,
 * в десятичную систему и вернуть как результат.
 * Римские цифры: 1 = I, 4 = IV, 5 = V, 9 = IX, 10 = X, 40 = XL, 50 = L,
 * 90 = XC, 100 = C, 400 = CD, 500 = D, 900 = CM, 1000 = M.
 * Например: XXIII = 23, XLIV = 44, C = 100
 *
 * Вернуть -1, если roman не является корректным римским числом
 */
fun fromRoman(roman: String): Int {
    if (roman.isEmpty()) return -1
    val revRoman = roman.reversed()
    val values = mapOf('I' to 1, 'V' to 5, 'X' to 10, 'L' to 50, 'C' to 100, 'D' to 500, 'M' to 1000)
    var number = values[revRoman[0]] ?: -1
    if (number == -1) return -1
    for (i in 1 until revRoman.length) {
        val value = values[revRoman[i]] ?: return -1
        if (value >= values[revRoman[i - 1]]!!)
            number += value
        else
            number -= value
    }
    return number
}

/**
 * Очень сложная (7 баллов)
 *
 * Имеется специальное устройство, представляющее собой
 * конвейер из cells ячеек (нумеруются от 0 до cells - 1 слева направо) и датчик, двигающийся над этим конвейером.
 * Строка commands содержит последовательность команд, выполняемых данным устройством, например +>+>+>+>+
 * Каждая команда кодируется одним специальным символом:
 *	> - сдвиг датчика вправо на 1 ячейку;
 *  < - сдвиг датчика влево на 1 ячейку;
 *	+ - увеличение значения в ячейке под датчиком на 1 ед.;
 *	- - уменьшение значения в ячейке под датчиком на 1 ед.;
 *	[ - если значение под датчиком равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей следующей командой ']' (с учётом вложенности);
 *	] - если значение под датчиком не равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей предыдущей командой '[' (с учётом вложенности);
 *      (комбинация [] имитирует цикл)
 *  пробел - пустая команда
 *
 * Изначально все ячейки заполнены значением 0 и датчик стоит на ячейке с номером N/2 (округлять вниз)
 *
 * После выполнения limit команд или всех команд из commands следует прекратить выполнение последовательности команд.
 * Учитываются все команды, в том числе несостоявшиеся переходы ("[" при значении под датчиком не равном 0 и "]" при
 * значении под датчиком равном 0) и пробелы.
 *
 * Вернуть список размера cells, содержащий элементы ячеек устройства после завершения выполнения последовательности.
 * Например, для 10 ячеек и командной строки +>+>+>+>+ результат должен быть 0,0,0,0,0,1,1,1,1,1
 *
 * Все прочие символы следует считать ошибочными и формировать исключение IllegalArgumentException.
 * То же исключение формируется, если у символов [ ] не оказывается пары.
 * Выход за границу конвейера также следует считать ошибкой и формировать исключение IllegalStateException.
 * Считать, что ошибочные символы и непарные скобки являются более приоритетной ошибкой чем выход за границу ленты,
 * то есть если в программе присутствует некорректный символ или непарная скобка, то должно быть выброшено
 * IllegalArgumentException.
 * IllegalArgumentException должен бросаться даже если ошибочная команда не была достигнута в ходе выполнения.
 *
 */

fun checkBracketsAndCommands(str: String): Boolean {
    var height = 0
    for (char in str) {
        if (char !in "+-<>[] ")
            return false
        if (char == '[')
            height++
        else if (char == ']')
            height--
        if (height < 0)
            return false
    }
    return height == 0
}

fun computeDeviceCells(cells: Int, commands: String, limit: Int): List<Int> {
    if (!checkBracketsAndCommands(commands))
        throw IllegalArgumentException()
    val memory = mutableListOf<Int>()
    for (i in 1..cells)
        memory.add(0)
    var ptr = floor(cells / 2.0).toInt()
    var operationCounter = 1
    var i = 0
    var bracketLvl = 0
    while (i < commands.length) {
        when (commands[i]) {
            '+' -> memory[ptr]++
            '-' -> memory[ptr]--
            '>' -> ptr++
            '<' -> ptr--
            '[' -> if (memory[ptr] == 0) {
                bracketLvl++
                while (bracketLvl != 0) {
                    i++
                    if (commands[i] == '[')
                        bracketLvl++
                    else if (commands[i] == ']')
                        bracketLvl--
                }
            }

            ']' -> if (memory[ptr] != 0) {
                bracketLvl = 1
                while (bracketLvl != 0) {
                    i--
                    if (commands[i] == '[')
                        bracketLvl--
                    else if (commands[i] == ']')
                        bracketLvl++
                }
                i--
                operationCounter--
            }

            else -> {}
        }
        i++
        operationCounter++
        if (operationCounter > limit)
            return memory
        if (ptr !in 0 until cells)
            throw IllegalStateException()
    }
    return memory
}
