@file:Suppress("UNUSED_PARAMETER")

package lesson8.task1

import lesson1.task1.sqr
import kotlin.IllegalArgumentException
import kotlin.math.*

// Урок 8: простые классы
// Максимальное количество баллов = 40 (без очень трудных задач = 11)

/**
 * Точка на плоскости
 */
data class Point(val x: Double, val y: Double) {
    /**
     * Пример
     *
     * Рассчитать (по известной формуле) расстояние между двумя точками
     */
    fun distance(other: Point): Double = sqrt(sqr(x - other.x) + sqr(y - other.y))
}

/**
 * Треугольник, заданный тремя точками (a, b, c, см. constructor ниже).
 * Эти три точки хранятся в множестве points, их порядок не имеет значения.
 */
@Suppress("MemberVisibilityCanBePrivate")
class Triangle private constructor(private val points: Set<Point>) {

    private val pointList = points.toList()

    val a: Point get() = pointList[0]

    val b: Point get() = pointList[1]

    val c: Point get() = pointList[2]

    constructor(a: Point, b: Point, c: Point) : this(linkedSetOf(a, b, c))

    /**
     * Пример: полупериметр
     */
    fun halfPerimeter() = (a.distance(b) + b.distance(c) + c.distance(a)) / 2.0

    /**
     * Пример: площадь
     */
    fun area(): Double {
        val p = halfPerimeter()
        return sqrt(p * (p - a.distance(b)) * (p - b.distance(c)) * (p - c.distance(a)))
    }

    /**
     * Пример: треугольник содержит точку
     */
    fun contains(p: Point): Boolean {
        val abp = Triangle(a, b, p)
        val bcp = Triangle(b, c, p)
        val cap = Triangle(c, a, p)
        return abp.area() + bcp.area() + cap.area() <= area()
    }

    override fun equals(other: Any?) = other is Triangle && points == other.points

    override fun hashCode() = points.hashCode()

    override fun toString() = "Triangle(a = $a, b = $b, c = $c)"
}

/**
 * Окружность с заданным центром и радиусом
 */
data class Circle(val center: Point, val radius: Double) {
    /**
     * Простая (2 балла)
     *
     * Рассчитать расстояние между двумя окружностями.
     * Расстояние между непересекающимися окружностями рассчитывается как
     * расстояние между их центрами минус сумма их радиусов.
     * Расстояние между пересекающимися окружностями считать равным 0.0.
     */
    fun distance(other: Circle): Double = max(0.0, this.center.distance(other.center) - this.radius - other.radius)

    /**
     * Тривиальная (1 балл)
     *
     * Вернуть true, если и только если окружность содержит данную точку НА себе или ВНУТРИ себя
     */
    fun contains(p: Point): Boolean = p.distance(this.center) <= radius
}

/**
 * Отрезок между двумя точками
 */
data class Segment(val begin: Point, val end: Point) {
    override fun equals(other: Any?) =
        other is Segment && (begin == other.begin && end == other.end || end == other.begin && begin == other.end)

    override fun hashCode() =
        begin.hashCode() + end.hashCode()

    fun length() = begin.distance(end)

    fun midPoint() = Point((begin.x + end.x) / 2, (begin.y + end.y) / 2)
}

/**
 * Средняя (3 балла)
 *
 * Дано множество точек. Вернуть отрезок, соединяющий две наиболее удалённые из них.
 * Если в множестве менее двух точек, бросить IllegalArgumentException
 */
fun diameter(vararg points: Point): Segment {
    if (points.size < 2) throw IllegalArgumentException()
    var diam = Segment(points[0], points[1])
    for (i in points.indices) {
        for (j in i + 1 until points.size) {
            if (points[i].distance(points[j]) > diam.length()) {
                diam = Segment(points[i], points[j])
            }
        }
    }
    return diam
}

/**
 * Простая (2 балла)
 *
 * Построить окружность по её диаметру, заданному двумя точками
 * Центр её должен находиться посередине между точками, а радиус составлять половину расстояния между ними
 */
fun circleByDiameter(diameter: Segment): Circle = Circle(diameter.midPoint(), diameter.length() / 2)

/**
 * Прямая, заданная точкой point и углом наклона angle (в радианах) по отношению к оси X.
 * Уравнение прямой: (y - point.y) * cos(angle) = (x - point.x) * sin(angle)
 * или: y * cos(angle) = x * sin(angle) + b, где b = point.y * cos(angle) - point.x * sin(angle).
 * Угол наклона обязан находиться в диапазоне от 0 (включительно) до PI (исключительно).
 */
class Line private constructor(val b: Double, val angle: Double) {
    init {
        require(angle >= 0 && angle < PI) { "Incorrect line angle: $angle" }
    }

    constructor(point: Point, angle: Double) : this(point.y * cos(angle) - point.x * sin(angle), angle)

    /**
     * Средняя (3 балла)
     *
     * Найти точку пересечения с другой линией.
     * Для этого необходимо составить и решить систему из двух уравнений (каждое для своей прямой)
     */
    fun crossPoint(other: Line): Point {
        val detMain = sin(other.angle - this.angle)
        if (abs(detMain) < Math.ulp(10.0)) throw Exception("Lines are parallel or the same")
        val detX = this.b * cos(other.angle) - other.b * cos(this.angle)
        val detY = this.b * sin(other.angle) - other.b * sin(this.angle)
        return Point(detX / detMain, detY / detMain)
    }

    override fun equals(other: Any?) = other is Line && angle == other.angle && b == other.b

    override fun hashCode(): Int {
        var result = b.hashCode()
        result = 31 * result + angle.hashCode()
        return result
    }

    override fun toString() = "Line(${cos(angle)} * y = ${sin(angle)} * x + $b)"
}

/**
 * Средняя (3 балла)
 *
 * Построить прямую по отрезку
 */
fun lineBySegment(s: Segment): Line = Line(s.begin, acos((s.end.x - s.begin.x) / s.length()))

/**
 * Средняя (3 балла)
 *
 * Построить прямую по двум точкам
 */
fun lineByPoints(a: Point, b: Point): Line = Line(a, acos((b.x - a.x) / a.distance(b)))

/**
 * Сложная (5 баллов)
 *
 * Построить серединный перпендикуляр по отрезку или по двум точкам
 */
fun bisectorByPoints(a: Point, b: Point): Line {
    val origin = Segment(a, b).midPoint()
    var vecX = b.x - a.x
    var vecY = b.y - a.y
    if (vecY < 0) {
        vecY *= -1
        vecX *= -1
    }
    var normVecX = vecY
    var normVecY = -vecX
    if (normVecY < 0) {
        normVecY *= -1
        normVecX *= -1
    }
    return lineByPoints(origin, Point(origin.x + normVecX, origin.y + normVecY))
}

/**
 * Средняя (3 балла)
 *
 * Задан список из n окружностей на плоскости.
 * Найти пару наименее удалённых из них; расстояние между окружностями
 * рассчитывать так, как указано в Circle.distance.
 *
 * При наличии нескольких наименее удалённых пар,
 * вернуть первую из них по порядку в списке circles.
 *
 * Если в списке менее двух окружностей, бросить IllegalArgumentException
 */
fun findNearestCirclePair(vararg circles: Circle): Pair<Circle, Circle> {
    if (circles.size < 2) throw IllegalArgumentException()
    var ans = Pair(circles[0], circles[1])
    var minDist = ans.first.distance(ans.second)
    for (i in circles.indices) {
        for (j in i + 1 until circles.size) {
            val dst = circles[i].distance(circles[j])
            if (dst < minDist) {
                ans = Pair(circles[i], circles[j])
                minDist = dst
            }
        }
    }
    return ans
}

/**
 * Сложная (5 баллов)
 *
 * Дано три различные точки. Построить окружность, проходящую через них
 * (все три точки должны лежать НА, а не ВНУТРИ, окружности).
 * Описание алгоритмов см. в Интернете
 * (построить окружность по трём точкам, или
 * построить окружность, описанную вокруг треугольника - эквивалентная задача).
 */
fun circleByThreePoints(a: Point, b: Point, c: Point): Circle {
    val auxLine1 = bisectorByPoints(a, b)
    val auxLine2 = bisectorByPoints(b, c)
    val center = auxLine1.crossPoint(auxLine2)
    return Circle(center, center.distance(a))
}

/**
 * Очень сложная (10 баллов)
 *
 * Дано множество точек на плоскости. Найти круг минимального радиуса,
 * содержащий все эти точки. Если множество пустое, бросить IllegalArgumentException.
 * Если множество содержит одну точку, вернуть круг нулевого радиуса с центром в данной точке.
 *
 * Примечание: в зависимости от ситуации, такая окружность может либо проходить через какие-либо
 * три точки данного множества, либо иметь своим диаметром отрезок,
 * соединяющий две самые удалённые точки в данном множестве.
 */
fun minContainingCircle(vararg points: Point): Circle {
    TODO()
//    when (points.size) {
//        0 -> throw IllegalArgumentException()
//        1 -> return Circle(points[0], 0.0)
//        2 -> return circleByDiameter(Segment(points[0], points[1]))
//        3 -> return circleByThreePoints(points[0], points[1], points[2])
//    }
//    var ans = circleByThreePoints(points[0], points[1], points[2])
//    val pts = mutableListOf(points[0], points[1], points[2])
//    var test: Circle
//    for (i in 4 until points.size) {
//        if (ans.contains(points[i])) continue
//        var minR = Double.MAX_VALUE
//        var temppts = pts
//        try {
//            ans = circleByThreePoints(points[i], pts[1], pts[2])
//            if (ans.contains(pts[0])) {
//                minR = ans.radius
//                temppts[0] = points[i]
//            }
//        } catch (_: Exception) { null }
//        try {
//            test = circleByThreePoints(pts[0], points[i], pts[2])
//            if (test.contains(pts[1]) && test.radius < minR) {
//                minR = test.radius
//                temppts = pts
//                temppts[1] = points[i]
//                ans = test
//            }
//        } catch (_: Exception) { null }
//        try {
//            test = circleByThreePoints(pts[0], pts[1], points[i])
//            if (test.contains(pts[2]) && test.radius < minR) {
//                minR = test.radius
//                temppts = pts
//                temppts[1] = points[i]
//                ans = test
//            }
//        } catch (_: IllegalArgumentException) { null }
//    }
//    test = circleByDiameter(diameter(*points))
//    if (test.radius > ans.radius || points.any { !test.contains(it) })
//        return ans
//    return test
}

