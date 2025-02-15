package me.kzv.refactoring.chapter01.example

import me.kzv.refactoring.chapter01.Invoice
import me.kzv.refactoring.chapter01.Play
import me.kzv.refactoring.chapter01.invoices
import me.kzv.refactoring.chapter01.plays
import java.text.NumberFormat
import java.util.*

/**
 * 원본 예제 그대로 kotlin으로 작성해보기
 */
fun statement(invoice: Invoice, plays: Map<String, Play>): String {
    var totalAmount = 0
    var volumeCredits = 0
    var result = "청구 내역 (고객명: ${invoice.customer})\n"
    val format = NumberFormat.getCurrencyInstance(Locale.US)

    for (perf in invoice.performances) {
        val play = plays[perf.playID]
        var thisAmount = 0

        when (play?.type) {
            "tragedy" -> {
                thisAmount = 40000
                if (perf.audience > 30) {
                    thisAmount += 1000 * (perf.audience - 30)
                }
            }
            "comedy" -> {
                thisAmount = 30000
                if (perf.audience > 20) {
                    thisAmount += 10000 + 500 * (perf.audience - 20)
                }
                thisAmount += 300 * perf.audience
            }
            else -> throw IllegalArgumentException("알 수 없는 장르: ${play?.type}")
        }

        // 포인트를 적립한다.
        volumeCredits += Math.max(perf.audience - 30, 0)
        // 희극 관객 5명마다 추가 포인트를 제공한다.
        if("comedy" == play.type) volumeCredits += perf.audience / 5 // 원본 js 코드는 Math.floor 처리

        // 청구 내역을 출력한다.
        result += " ${play.name}: ${format.format(thisAmount / 100.0)} (${perf.audience}석)\n"
        totalAmount += thisAmount
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