package me.kzv.refactoring.chapter01.step01

import me.kzv.refactoring.chapter01.*
import java.text.NumberFormat
import java.util.*

/**
 * step01 - 1.4 statement 함수 쪼개기
 */
fun statement(invoice: Invoice, plays: Map<String, Play>): String {
    var totalAmount = 0
    var volumeCredits = 0
    var result = "청구 내역 (고객명: ${invoice.customer})\n"
    val format = NumberFormat.getCurrencyInstance(Locale.US)

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

    for (perf in invoice.performances) {
        // 포인트를 적립한다.
        volumeCredits += Math.max(perf.audience - 30, 0)
        // 희극 관객 5명마다 추가 포인트를 제공한다.
        if("comedy" == playFor(perf).type) volumeCredits += perf.audience / 5 // 원본 js 코드는 Math.floor 처리

        // 청구 내역을 출력한다.
        result += " ${playFor(perf).name}: ${format.format(amountFor(perf) / 100.0)} (${perf.audience}석)\n"
        totalAmount += amountFor(perf)
    }

    result += "총액: ${format.format(totalAmount / 100.0)}\n"
    result += "적립 포인트: ${volumeCredits}점\n"
    return result
}

fun main() {
    for (invoice in invoices) {
        val result = statement(invoice, plays)
        println(result)
    }
}