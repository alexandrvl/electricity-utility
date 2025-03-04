package com.electricity.utility.domain.billing.service

import com.electricity.utility.domain.billing.model.*
import com.electricity.utility.domain.authentication.repository.UserRepository
import java.time.LocalDateTime
import java.math.BigDecimal

class BillingImpl(
    private val userRepository: UserRepository
) : Billing {
    private val bills = mutableMapOf<String, Bill>()
    private val payments = mutableMapOf<String, MutableList<Payment>>()

    override fun generateMonthlyBills(): List<Bill> {
        val activeAccounts = getActiveAccounts()
        val currentPeriod = BillingPeriod(
            startDate = LocalDateTime.now()
                .withDayOfMonth(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0),
            endDate = LocalDateTime.now()
                .withDayOfMonth(LocalDateTime.now().month.length(LocalDateTime.now().toLocalDate().isLeapYear))
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
        )

        return activeAccounts.map { customerId ->
            val consumptionDetails = generateConsumptionDetails(customerId, currentPeriod)
            val bill = Bill(
                billId = "BILL-${System.currentTimeMillis()}-$customerId",
                customerId = customerId,
                billingPeriod = currentPeriod,
                amount = calculateBillAmount(consumptionDetails),
                dueDate = currentPeriod.endDate.plusDays(14),
                status = BillStatus.GENERATED,
                consumptionDetails = consumptionDetails
            )
            bills[bill.billId] = bill
            bill
        }
    }

    override fun getCurrentBill(customerId: String): Bill? {
        return bills.values.find { it.customerId == customerId && it.status != BillStatus.PAID }
    }

    override fun getPaymentHistory(customerId: String): List<Payment> {
        return payments[customerId] ?: emptyList()
    }

    override fun isBillingSectionAccessible(customerId: String): Boolean {
        return userRepository.isUserActive(customerId)
    }

    override fun getActiveAccounts(): List<String> {
        return userRepository.getActiveUsers()
    }

    override fun isConsumptionDataComplete(customerId: String, period: BillingPeriod): Boolean {
        val consumptionDetails = generateConsumptionDetails(customerId, period)
        return consumptionDetails.readings.isNotEmpty() &&
                consumptionDetails.totalKilowattHours > 0
    }

    private fun generateConsumptionDetails(customerId: String, period: BillingPeriod): ConsumptionDetails {
        // Simulate consumption readings
        val readings = mutableListOf<ConsumptionReading>()
        var currentDate = period.startDate
        var totalPeakUsage = 0.0
        var totalOffPeakUsage = 0.0

        while (!currentDate.isAfter(period.endDate)) {
            val isPeakHours = currentDate.hour in 9..17
            val usage = if (isPeakHours) 2.5 else 1.0

            readings.add(ConsumptionReading(
                timestamp = currentDate,
                kilowattHours = usage,
                isPeakHours = isPeakHours
            ))

            if (isPeakHours) totalPeakUsage += usage else totalOffPeakUsage += usage
            currentDate = currentDate.plusHours(1)
        }

        return ConsumptionDetails(
            totalKilowattHours = totalPeakUsage + totalOffPeakUsage,
            peakUsageKilowattHours = totalPeakUsage,
            offPeakUsageKilowattHours = totalOffPeakUsage,
            readings = readings
        )
    }

    private fun calculateBillAmount(consumptionDetails: ConsumptionDetails): BigDecimal {
        val peakRate = BigDecimal("0.15") // $0.15 per kWh during peak hours
        val offPeakRate = BigDecimal("0.08") // $0.08 per kWh during off-peak hours

        return (peakRate * BigDecimal(consumptionDetails.peakUsageKilowattHours) +
                offPeakRate * BigDecimal(consumptionDetails.offPeakUsageKilowattHours))
            .setScale(2, BigDecimal.ROUND_HALF_UP)
    }
}
