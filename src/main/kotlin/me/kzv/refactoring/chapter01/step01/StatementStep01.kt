package me.kzv.refactoring.chapter01.step01

import me.kzv.refactoring.chapter01.*
import java.text.NumberFormat
import java.util.*

/**
 * step01 - 1.4 statement 함수 쪼개기
 *
 * - 저자는 임시 변수가 자신이 속한 루틴에서만 의미가 있어서 루틴이 길고 복잡해지기 쉬워 나중에 문제를 일으킬 수 있으므로 제거하는게 좋다고 제시하였다. // 동일한 값을 얻기 위해 반복적으로 함수를 실행하는 경우가 있는데 임시 변수를 제거하는게 꼭 좋은거 같지는 않아 보인다.
 */
fun statement(invoice: Invoice, plays: Map<String, Play>): String {
    var totalAmount = 0
    var volumeCredits = 0
    var result = "청구 내역 (고객명: ${invoice.customer})\n"

    fun format(aNumber: Int): String = NumberFormat.getCurrencyInstance(Locale.US).format(aNumber / 100.0)

    fun playFor(
        aPerformance: Performance // 명확한 이름으로 변경, 코드 스타일 중 매개변수의 역할이 뚜렷하지 않을 때는 부정관사를 붙여주는 방법도 있다. - 마땅한 이름을 짓기 애매해 고민을 오랫동안 할바에는 나쁘지 않은듯
    ) = plays[aPerformance.playID]!!

    // 서적에서는 중첩 함수로 변수를 줄일 수 있다고 재시함 하지만 여기서 play, perf는 반복문을 통해 동적으로 받으므로 매개변수로 받아 준다.
    fun amountFor(aPerformance: Performance): Int {
        var result = 0
        when (playFor(aPerformance).type) {
            "tragedy" -> {
                result = 40000
                if (aPerformance.audience > 30) {
                    result += 1000 * (aPerformance.audience - 30)
                }
            }

            "comedy" -> {
                result = 30000
                if (aPerformance.audience > 20) {
                    result += 10000 + 500 * (aPerformance.audience - 20)
                }
                result += 300 * aPerformance.audience
            }

            else -> throw IllegalArgumentException("알 수 없는 장르: ${playFor(aPerformance).type}")
        }
        return result
    }

    fun volumeCreditsFor(aPerformance: Performance) : Int {
        var result = 0
        result += Math.max(aPerformance.audience - 30, 0)
        if("comedy" == playFor(aPerformance).type) {
            result += aPerformance.audience / 5
        }
        return result
    }

    for (perf in invoice.performances) {
        volumeCredits += volumeCreditsFor(perf)

        // 청구 내역을 출력한다.
        result += " ${playFor(perf).name}: ${format(amountFor(perf))} (${perf.audience}석)\n"
        totalAmount += amountFor(perf)
    }

    result += "총액: ${format(totalAmount)}\n"
    result += "적립 포인트: ${volumeCredits}점\n"
    return result
}

fun main() {
    for (invoice in invoices) {
        val result = statement(invoice, plays)
        println(result)
    }
}