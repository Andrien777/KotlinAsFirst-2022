@file:Suppress("UNUSED_PARAMETER")

package lesson9.task2

import lesson9.task1.Matrix
import lesson9.task1.createMatrix

// Все задачи в этом файле требуют наличия реализации интерфейса "Матрица" в Matrix.kt

/**
 * Пример
 *
 * Транспонировать заданную матрицу matrix.
 * При транспонировании строки матрицы становятся столбцами и наоборот:
 *
 * 1 2 3      1 4 6 3
 * 4 5 6  ==> 2 5 5 2
 * 6 5 4      3 6 4 1
 * 3 2 1
 */
fun <E> transpose(matrix: Matrix<E>): Matrix<E> {
    if (matrix.width < 1 || matrix.height < 1) return matrix
    val result = createMatrix(height = matrix.width, width = matrix.height, e = matrix[0, 0])
    for (i in 0 until matrix.width) {
        for (j in 0 until matrix.height) {
            result[i, j] = matrix[j, i]
        }
    }
    return result
}

/**
 * Пример
 *
 * Сложить две заданные матрицы друг с другом.
 * Складывать можно только матрицы совпадающего размера -- в противном случае бросить IllegalArgumentException.
 * При сложении попарно складываются соответствующие элементы матриц
 */
operator fun Matrix<Int>.plus(other: Matrix<Int>): Matrix<Int> {
    require(!(width != other.width || height != other.height))
    if (width < 1 || height < 1) return this
    val result = createMatrix(height, width, this[0, 0])
    for (i in 0 until height) {
        for (j in 0 until width) {
            result[i, j] = this[i, j] + other[i, j]
        }
    }
    return result
}

/**
 * Сложная (5 баллов)
 *
 * Заполнить матрицу заданной высоты height и ширины width
 * натуральными числами от 1 до m*n по спирали,
 * начинающейся в левом верхнем углу и закрученной по часовой стрелке.
 *
 * Пример для height = 3, width = 4:
 *  1  2  3  4
 * 10 11 12  5
 *  9  8  7  6
 */
fun generateSpiral(height: Int, width: Int): Matrix<Int> {
    val ans = createMatrix(height, width, 0)
    var i = 0
    var j = 0
    var counter = 1
    var dir = 0
    while (counter <= height * width) {
        ans[i, j] = counter
        val backupDir = dir
        when (dir) {
            0 -> if ((j < width - 1) && ans[i, j + 1] == 0) j++ else dir++
            1 -> if ((i < height - 1) && ans[i + 1, j] == 0) i++ else dir++
            2 -> if ((j > 0) && ans[i, j - 1] == 0) j-- else dir++
            3 -> if ((i > 0) && ans[i - 1, j] == 0) i-- else dir++
        }
        dir %= 4
        if (dir != backupDir) {
            when (dir) {
                0 -> if ((j < width - 1) && ans[i, j + 1] == 0) j++ else dir++
                1 -> if ((i < height - 1) && ans[i + 1, j] == 0) i++ else dir++
                2 -> if ((j > 0) && ans[i, j - 1] == 0) j-- else dir++
                3 -> if ((i > 0) && ans[i - 1, j] == 0) i-- else dir++
            }
            dir %= 4
        }
        counter++
    }
    return ans
}

/**
 * Сложная (5 баллов)
 *
 * Заполнить матрицу заданной высоты height и ширины width следующим образом.
 * Элементам, находящимся на периферии (по периметру матрицы), присвоить значение 1;
 * периметру оставшейся подматрицы – значение 2 и так далее до заполнения всей матрицы.
 *
 * Пример для height = 5, width = 6:
 *  1  1  1  1  1  1
 *  1  2  2  2  2  1
 *  1  2  3  3  2  1
 *  1  2  2  2  2  1
 *  1  1  1  1  1  1
 */
fun generateRectangles(height: Int, width: Int): Matrix<Int> {
    fun distToEdge(i: Int, j: Int) = minOf(i, j, height - i - 1, width - j - 1)
    val ans = createMatrix(height, width, 0)
    for (i in 0 until height) {
        for (j in 0 until width) {
            ans[i, j] = distToEdge(i, j) + 1
        }
    }
    return ans
}

/**
 * Сложная (5 баллов)
 *
 * Заполнить матрицу заданной высоты height и ширины width диагональной змейкой:
 * в левый верхний угол 1, во вторую от угла диагональ 2 и 3 сверху вниз, в третью 4-6 сверху вниз и так далее.
 *
 * Пример для height = 5, width = 4:
 *  1  2  4  7
 *  3  5  8 11
 *  6  9 12 15
 * 10 13 16 18
 * 14 17 19 20
 */
fun generateSnake(height: Int, width: Int): Matrix<Int> {
    val ans = createMatrix(height, width, 0)
    var counter = 1
    var sum = 0
    while (counter <= height * width) {
        for (i in 0 until height) {
            for (j in 0 until width) {
                if (i + j == sum) {
                    ans[i, j] = counter
                    counter++
                }
            }
        }
        sum++
    }
    return ans
}

/**
 * Средняя (3 балла)
 *
 * Содержимое квадратной матрицы matrix (с произвольным содержимым) повернуть на 90 градусов по часовой стрелке.
 * Если height != width, бросить IllegalArgumentException.
 *
 * Пример:    Станет:
 * 1 2 3      7 4 1
 * 4 5 6      8 5 2
 * 7 8 9      9 6 3
 */
fun <E> rotate(matrix: Matrix<E>): Matrix<E> {
    if (matrix.width != matrix.height) throw IllegalArgumentException()
    val ans = createMatrix(matrix.height, matrix.width, matrix[0, 0])
    for (i in 0 until matrix.height) {
        for (j in 0 until matrix.width) {
            ans[j, matrix.width - i - 1] = matrix[i, j]
        }
    }
    return ans
}

/**
 * Сложная (5 баллов)
 *
 * Проверить, является ли квадратная целочисленная матрица matrix латинским квадратом.
 * Латинским квадратом называется матрица размером n x n,
 * каждая строка и каждый столбец которой содержат все числа от 1 до n.
 * Если height != width, вернуть false.
 *
 * Пример латинского квадрата 3х3:
 * 2 3 1
 * 1 2 3
 * 3 1 2
 */
fun isLatinSquare(matrix: Matrix<Int>): Boolean {
    if (matrix.height != matrix.width) return false
    val n = matrix.height
    for (i in 0 until n) {
        val row = List(n) { matrix[i, it] }.toSet()
        val col = List(n) { matrix[it, i] }.toSet()
        if (row != (1..n).toSet() || col != (1..n).toSet()) return false
    }
    return true
}

/**
 * Средняя (3 балла)
 *
 * В матрице matrix каждый элемент заменить суммой непосредственно примыкающих к нему
 * элементов по вертикали, горизонтали и диагоналям.
 *
 * Пример для матрицы 4 x 3: (11=2+4+5, 19=1+3+4+5+6, ...)
 * 1 2 3       11 19 13
 * 4 5 6  ===> 19 31 19
 * 6 5 4       19 31 19
 * 3 2 1       13 19 11
 *
 * Поскольку в матрице 1 х 1 примыкающие элементы отсутствуют,
 * для неё следует вернуть как результат нулевую матрицу:
 *
 * 42 ===> 0
 */
fun sumNeighbours(matrix: Matrix<Int>): Matrix<Int> {
    val ans = createMatrix(matrix.height, matrix.width, 0)
    if (matrix.height == 1 && matrix.width == 1) return ans
    for (i in 0 until matrix.height) {
        for (j in 0 until matrix.width) {
            var sum = 0
            for (dx in -1..1) {
                for (dy in -1..1) {
                    if (dx != 0 || dy != 0) {
                        if (j + dx in 0 until matrix.width && i + dy in 0 until matrix.height) {
                            sum += matrix[i + dy, j + dx]
                        }
                    }
                }
            }
            ans[i, j] = sum
        }
    }
    return ans
}

/**
 * Средняя (4 балла)
 *
 * Целочисленная матрица matrix состоит из "дырок" (на их месте стоит 0) и "кирпичей" (на их месте стоит 1).
 * Найти в этой матрице все ряды и колонки, целиком состоящие из "дырок".
 * Результат вернуть в виде Holes(rows = список дырчатых рядов, columns = список дырчатых колонок).
 * Ряды и колонки нумеруются с нуля. Любой из спискоов rows / columns может оказаться пустым.
 *
 * Пример для матрицы 5 х 4:
 * 1 0 1 0
 * 0 0 1 0
 * 1 0 0 0 ==> результат: Holes(rows = listOf(4), columns = listOf(1, 3)): 4-й ряд, 1-я и 3-я колонки
 * 0 0 1 0
 * 0 0 0 0
 */
fun findHoles(matrix: Matrix<Int>): Holes {
    val rows = mutableListOf<Int>()
    val cols = mutableListOf<Int>()
    for (i in 0 until matrix.height) {
        var flag = true
        for (j in 0 until matrix.width) {
            if (matrix[i, j] != 0) {
                flag = false
                break
            }
        }
        if (flag) rows.add(i)
    }
    for (j in 0 until matrix.width) {
        var flag = true
        for (i in 0 until matrix.height) {
            if (matrix[i, j] != 0) {
                flag = false
                break
            }
        }
        if (flag) cols.add(j)
    }
    return Holes(rows, cols)
}

/**
 * Класс для описания местонахождения "дырок" в матрице
 */
data class Holes(val rows: List<Int>, val columns: List<Int>)

/**
 * Средняя (3 балла)
 *
 * В целочисленной матрице matrix каждый элемент заменить суммой элементов подматрицы,
 * расположенной в левом верхнем углу матрицы matrix и ограниченной справа-снизу данным элементом.
 *
 * Пример для матрицы 3 х 3:
 *
 * 1  2  3      1  3  6
 * 4  5  6  =>  5 12 21
 * 7  8  9     12 27 45
 *
 * К примеру, центральный элемент 12 = 1 + 2 + 4 + 5, элемент в левом нижнем углу 12 = 1 + 4 + 7 и так далее.
 */
fun sumSubMatrix(matrix: Matrix<Int>): Matrix<Int> {
    val ans = createMatrix(matrix.height, matrix.width, 0)
    for (i in 0 until matrix.height) {
        for (j in 0 until matrix.width) {
            var sum = 0
            for (sumI in 0..i) {
                for (sumJ in 0..j) {
                    sum += matrix[sumI, sumJ]
                }
            }
            ans[i, j] = sum
        }
    }
    return ans
}

/**
 * Простая (2 балла)
 *
 * Инвертировать заданную матрицу.
 * При инвертировании знак каждого элемента матрицы следует заменить на обратный
 */
operator fun Matrix<Int>.unaryMinus(): Matrix<Int> {
    val ans = createMatrix(this.height, this.width, 0)
    for (i in 0 until this.height) {
        for (j in 0 until this.width) {
            ans[i, j] = -this[i, j]
        }
    }
    return ans
}

/**
 * Средняя (4 балла)
 *
 * Перемножить две заданные матрицы друг с другом.
 * Матрицы можно умножать, только если ширина первой матрицы совпадает с высотой второй матрицы.
 * В противном случае бросить IllegalArgumentException.
 * Подробно про порядок умножения см. статью Википедии "Умножение матриц".
 */
operator fun Matrix<Int>.times(other: Matrix<Int>): Matrix<Int> {
    if (this.width != other.height) throw IllegalArgumentException()
    val ans = createMatrix(this.height, other.width, 0)
    for (i in 0 until this.height) {
        for (j in 0 until other.width) {
            var sum = 0
            for (k in 0 until this.width) {
                sum += this[i, k] * other[k, j]
            }
            ans[i, j] = sum
        }
    }
    return ans
}

/**
 * Сложная (7 баллов)
 *
 * Даны мозаичные изображения замочной скважины и ключа. Пройдет ли ключ в скважину?
 * То есть даны две матрицы key и lock, key.height <= lock.height, key.width <= lock.width, состоящие из нулей и единиц.
 *
 * Проверить, можно ли наложить матрицу key на матрицу lock (без поворота, разрешается только сдвиг) так,
 * чтобы каждой единице в матрице lock (штырь) соответствовал ноль в матрице key (прорезь),
 * а каждому нулю в матрице lock (дырка) соответствовала, наоборот, единица в матрице key (штырь).
 * Ключ при сдвиге не может выходить за пределы замка.
 *
 * Пример: ключ подойдёт, если его сдвинуть на 1 по ширине
 * lock    key
 * 1 0 1   1 0
 * 0 1 0   0 1
 * 1 1 1
 *
 * Вернуть тройку (Triple) -- (да/нет, требуемый сдвиг по высоте, требуемый сдвиг по ширине).
 * Если наложение невозможно, то первый элемент тройки "нет" и сдвиги могут быть любыми.
 */
fun canOpenLock(key: Matrix<Int>, lock: Matrix<Int>): Triple<Boolean, Int, Int> {
    for (dx in 0..lock.width - key.width) {
        for (dy in 0..lock.height - key.height) {
            var flag = true
            for (i in 0 until key.height) {
                for (j in 0 until key.width) {
                    if (key[i, j] + lock[i + dy, j + dx] != 1) {
                        flag = false
                        break
                    }
                }
                if (!flag) break
            }
            if (flag) return Triple(true, dy, dx)
        }
    }
    return Triple(false, -1, -1)
}

/**
 * Сложная (8 баллов)
 *
 * В матрице matrix размером 4х4 дана исходная позиция для игры в 15, например
 *  5  7  9  1
 *  2 12 14 15
 *  3  4  6  8
 * 10 11 13  0
 *
 * Здесь 0 обозначает пустую клетку, а 1-15 – фишки с соответствующими номерами.
 * Напомним, что "игра в 15" имеет квадратное поле 4х4, по которому двигается 15 фишек,
 * одна клетка всегда остаётся пустой. Цель игры -- упорядочить фишки на игровом поле.
 *
 * В списке moves задана последовательность ходов, например [8, 6, 13, 11, 10, 3].
 * Ход задаётся номером фишки, которая передвигается на пустое место (то есть, меняется местами с нулём).
 * Фишка должна примыкать к пустому месту по горизонтали или вертикали, иначе ход не будет возможным.
 * Все номера должны быть в пределах от 1 до 15.
 * Определить финальную позицию после выполнения всех ходов и вернуть её.
 * Если какой-либо ход является невозможным или список содержит неверные номера,
 * бросить IllegalStateException.
 *
 * В данном случае должно получиться
 * 5  7  9  1
 * 2 12 14 15
 * 0  4 13  6
 * 3 10 11  8
 */
fun fifteenGameMoves(matrix: Matrix<Int>, moves: List<Int>): Matrix<Int> {
    val ans = createMatrix(4, 4, 0)
    for (i in 0..3) {
        for (j in 0..3) {
            ans[i, j] = matrix[i, j]
        }
    }
    for (move in moves) {
        var x = -1
        var y = -1
        for (i in 0..3) {
            for (j in 0..3) {
                if (ans[i, j] == move) {
                    x = i
                    y = j
                    break
                }
            }
            if (x > 0) break
        }
        if (x < 0) throw IllegalStateException()
        for (dx in -1..1) {
            if (dx == 0) {
                for (dy in -1..1) {
                    if (dy != 0) {
                        if (y + dy in 0..3) {
                            if (ans[x, y + dy] == 0) {
                                ans[x, y] = 0
                                ans[x, y + dy] = move
                                break
                            }
                        }
                    }
                }
                if (ans[x, y] == 0) break
            } else {
                if (x + dx in 0..3) {
                    if (ans[x + dx, y] == 0) {
                        ans[x, y] = 0
                        ans[x + dx, y] = move
                        break
                    }
                }
            }
        }
        if (ans[x, y] != 0) throw IllegalStateException()
    }
    return ans
}

/**
 * Очень сложная (32 балла)
 *
 * В матрице matrix размером 4х4 дана исходная позиция для игры в 15, например
 *  5  7  9  2
 *  1 12 14 15
 *  3  4  6  8
 * 10 11 13  0
 *
 * Здесь 0 обозначает пустую клетку, а 1-15 – фишки с соответствующими номерами.
 * Напомним, что "игра в 15" имеет квадратное поле 4х4, по которому двигается 15 фишек,
 * одна клетка всегда остаётся пустой.
 *
 * Цель игры -- упорядочить фишки на игровом поле, приведя позицию к одному из следующих двух состояний:
 *
 *  1  2  3  4          1  2  3  4
 *  5  6  7  8   ИЛИ    5  6  7  8
 *  9 10 11 12          9 10 11 12
 * 13 14 15  0         13 15 14  0
 *
 * Можно математически доказать, что РОВНО ОДНО из этих двух состояний достижимо из любой исходной позиции.
 *
 * Вернуть решение -- список ходов, приводящих исходную позицию к одной из двух упорядоченных.
 * Каждый ход -- это перемена мест фишки с заданным номером с пустой клеткой (0),
 * при этом заданная фишка должна по горизонтали или по вертикали примыкать к пустой клетке (но НЕ по диагонали).
 * К примеру, ход 13 в исходной позиции меняет местами 13 и 0, а ход 11 в той же позиции невозможен.
 *
 * Одно из решений исходной позиции:
 *
 * [8, 6, 14, 12, 4, 11, 13, 14, 12, 4,
 * 7, 5, 1, 3, 11, 7, 3, 11, 7, 12, 6,
 * 15, 4, 9, 2, 4, 9, 3, 5, 2, 3, 9,
 * 15, 8, 14, 13, 12, 7, 11, 5, 7, 6,
 * 9, 15, 8, 14, 13, 9, 15, 7, 6, 12,
 * 9, 13, 14, 15, 12, 11, 10, 9, 13, 14,
 * 15, 12, 11, 10, 9, 13, 14, 15]
 *
 * Перед решением этой задачи НЕОБХОДИМО решить предыдущую
 */
fun fifteenGameSolution(matrix: Matrix<Int>): List<Int> = TODO()
