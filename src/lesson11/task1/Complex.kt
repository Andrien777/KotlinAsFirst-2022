@file:Suppress("UNUSED_PARAMETER")

package lesson11.task1

/**
 * Фабричный метод для создания комплексного числа из строки вида x+yi
 */
fun Complex(s: String): Complex {
    val numbers = s.replace("-", "+-").split("+")
    return Complex(numbers[0].toDouble(), numbers[1].removeSuffix("i").toDouble())
}

/**
 * Класс "комплексное число".
 *
 * Общая сложность задания -- лёгкая, общая ценность в баллах -- 8.
 * Объект класса -- комплексное число вида x+yi.
 * Про принципы работы с комплексными числами см. статью Википедии "Комплексное число".
 *
 * Аргументы конструктора -- вещественная и мнимая часть числа.
 */
class Complex(val re: Double, val im: Double) {

    /**
     * Конструктор из вещественного числа
     */
    constructor(x: Double) : this(x, .0)

    /**
     * Сложение.
     */
    operator fun plus(other: Complex): Complex = Complex(this.re + other.re, this.im + other.im)

    /**
     * Смена знака (у обеих частей числа)
     */
    operator fun unaryMinus(): Complex = Complex(-this.re, -this.im)

    /**
     * Вычитание
     */
    operator fun minus(other: Complex): Complex = this + (-other)

    /**
     * Умножение
     */
    operator fun times(other: Complex): Complex =
        Complex(this.re * other.re - this.im * other.im, this.im * other.re + this.re * other.im)

    /**
     * Деление
     */
    operator fun div(other: Complex): Complex {
        val conj = Complex(other.re, -other.im)
        val numer = this * conj
        val denom = (other * conj).re
        return Complex(numer.re / denom, numer.im / denom)
    }

    /**
     * Сравнение на равенство
     */
    override fun equals(other: Any?): Boolean = other is Complex &&
            this.re == other.re &&
            this.im == other.im

    /**
     * Преобразование в строку
     */
    override fun toString(): String =
        "${this.re}${if (this.im >= 0) "+" else "-"}${this.im}"

    override fun hashCode(): Int {
        return this.re.hashCode() + 7 * this.im.hashCode()
    }
}
