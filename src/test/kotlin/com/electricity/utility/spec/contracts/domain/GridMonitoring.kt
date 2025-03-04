package test.spec.contracts.domain

/**
 * Represents the current status of a grid section
 */
data class GridStatus(
    val sectionId: String,
    val powerLevel: Double,
    val voltage: Double,
    val frequency: Double,
    val isStable: Boolean,
    val timestamp: Long
)

/**
 * Represents a grid alert with its details
 */
data class GridAlert(
    val alertId: String,
    val sectionId: String,
    val severity: AlertSeverity,
    val message: String,
    val timestamp: Long,
    val details: Map<String, String>
)

/**
 * Defines possible severity levels for grid alerts
 */
enum class AlertSeverity {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}

/**
 * Contract for grid monitoring operations
 */
interface GridMonitoring {
    /**
     * Gets the real-time status of all grid sections
     * @return List of current grid statuses
     */
    fun getRealTimeGridStatus(): List<GridStatus>

    /**
     * Gets all active alerts in the system
     * @return List of active alerts
     */
    fun getActiveAlerts(): List<GridAlert>

    /**
     * Registers a notification handler for grid alerts
     * @param handler The function to handle new alerts
     */
    fun registerAlertHandler(handler: (GridAlert) -> Unit)

    /**
     * Gets detailed information about a specific alert
     * @param alertId The ID of the alert
     * @return The alert details if found, null otherwise
     */
    fun getAlertDetails(alertId: String): GridAlert?

    /**
     * Checks if the dashboard is currently accessible
     * @return true if the dashboard can be accessed, false otherwise
     */
    fun isDashboardAccessible(): Boolean
}