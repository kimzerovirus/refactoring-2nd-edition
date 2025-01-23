package me.kzv.refactoring.chapter01.refactor

import me.kzv.refactoring.chapter01.*
import java.text.NumberFormat
import java.util.*

/**
 * step01 - 1.4 statement 함수 쪼개기
 * - 저자는 임시 변수가 자신이 속한 루틴에서만 의미가 있어서 루틴이 길고 복잡해지기 쉬워 나중에 문제를 일으킬 수 있으므로 제거하는게 좋다고 제시하였다. // 동일한 값을 얻기 위해 반복적으로 함수를 실행하는 경우가 있는데 임시 변수를 제거하는게 꼭 좋은거 같지는 않아 보인다.
 * - volumeCredits >>> 반복문 쪼개기를 하며 저자는 이 정도의 중복으로 인해 미치는 성능 저하는 미미하므로 괜찮다고 제시, 물론 모든 경우에 적용되는 것은 아니므로 프로그래머 상황에 따라 판단해야 함
 *      >>> 저자는 성능 보다는 우선적으로 코드를 잘 다듬는게 더 중요하다 생각하는듯, 이를 통해 결과적으로 얻은 코드는 성능적으로 더 우수한 경우가 많았다고 함,
 *          >>> 저자의 조언은 "특별한 경우가 아니라면 일단 성능을 무시하고 리팩터링을 진행하자" 그 후 성능이 크게 덜어졌다면 성능 개선 작업을 진행하자고 함
 *
 * <volumeCredits 변수를 제거하는 작업의 단계>
 * 1. 반복문 쪼개기로 변수 값을 누적시키는 부분을 분리
 * 2. 문장 슬라이드하기로 변수 초기화 문장을 변수 값 누적 코드 바로 앞으로 옮기기
 * 3. 함수 추출하기로 적립 포인트 계산 부분을 별도 함수로 추출
 * 4. 변수 인라인하기로 volumeCredits 변수 제거
 *
 * step02 - 1.6 계산 단계와 포맷팅 단계 분리하기
 * - step01은 프로그램의 논리적인 요소를 파악하기 쉽도록 복잡한 덩어리를 잘게 쪼개 코드 구조를 보강하는데 주안점을 뒀다면, step02는 기능 단위??
 */
fun statement(invoice: Invoice, plays: Map<String, Play>): String {
    val statementData = Invoice(invoice.customer, invoice.performances)
    return renderPlainText(statementData, plays)
}

fun renderPlainText(data: Invoice, plays: Map<String, Play>): String {
    //=== 중첩 함수 시작 ===//
    fun usd(aNumber: Int): String = NumberFormat.getCurrencyInstance(Locale.US).format(aNumber / 100.0)

    fun playFor(
        aPerformance: Performance // 명확한 이름으로 변경, 코드 스타일 중 매개변수의 역할이 뚜렷하지 않을 때는 부정관사를 붙여주는 방법도 있다. - 마땅한 이름을 짓기 애매해 고민을 오랫동안 할바에는 나쁘지 않은듯
    ) = plays[aPerformance.playID]!!

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

    fun volumeCreditsFor(aPerformance: Performance): Int {
        var result = 0
        result += Math.max(aPerformance.audience - 30, 0)
        if ("comedy" == playFor(aPerformance).type) {
            result += aPerformance.audience / 5
        }
        return result
    }

    fun totalAmount(): Int {
        var result = 0
        for (perf in data.performances) {
            result += amountFor(perf)
        }
        return result
    }

    fun totalVolumeCredits(): Int {
        var volumeCredits = 0
        for (perf in data.performances) {
            volumeCredits += volumeCreditsFor(perf)
        }
        return volumeCredits
    }
    //=== 중첩 함수 끝 ===//

    var result = "청구 내역 (고객명: ${data.customer})\n"
    for (perf in data.performances) {
        // 청구 내역을 출력한다.
        result += " ${playFor(perf).name}: ${usd(amountFor(perf))} (${perf.audience}석)\n"
    }
    result += "총액: ${usd(totalAmount())}\n"
    result += "적립 포인트: ${totalVolumeCredits()}점\n"
    return result
}

fun main() {
    for (invoice in invoices) {
        val result = statement(invoice, plays)
        println(result)
    }
}