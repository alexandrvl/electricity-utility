package test.spec.contracts.domain

/**
 * Represents a user role with its permissions
 */
data class Role(
    val roleId: String,
    val name: String,
    val permissions: Set<Permission>,
    val features: Set<Feature>
)

/**
 * Represents a permission in the system
 */
data class Permission(
    val permissionId: String,
    val name: String,
    val description: String,
    val scope: PermissionScope
)

/**
 * Represents a feature in the system
 */
data class Feature(
    val featureId: String,
    val name: String,
    val requiredPermissions: Set<Permission>
)

/**
 * Represents an access denial event
 */
data class AccessDenial(
    val userId: String,
    val attemptedFeature: Feature,
    val requiredPermissions: Set<Permission>,
    val userPermissions: Set<Permission>,
    val message: String
)

/**
 * Defines permission scopes
 */
enum class PermissionScope {
    READ,
    WRITE,
    EXECUTE,
    ADMIN
}

/**
 * Defines system roles
 */
enum class SystemRole {
    ADMINISTRATOR,
    GRID_OPERATOR,
    SYSTEM_ANALYST,
    BILLING_ADMIN,
    CUSTOMER,
    GUEST
}

/**
 * Contract for role-based access control operations
 */
interface RoleBasedAccess {
    /**
     * Assigns a role to a user
     * @param userId The ID of the user
     * @param role The role to assign
     * @return true if role was assigned successfully, false otherwise
     */
    fun assignRole(userId: String, role: Role): Boolean

    /**
     * Gets the roles assigned to a user
     * @param userId The ID of the user
     * @return Set of roles assigned to the user
     */
    fun getUserRoles(userId: String): Set<Role>

    /**
     * Gets the permissions for a user
     * @param userId The ID of the user
     * @return Set of permissions the user has
     */
    fun getUserPermissions(userId: String): Set<Permission>

    /**
     * Checks if a user has access to a feature
     * @param userId The ID of the user
     * @param feature The feature to check
     * @return true if user has access, false otherwise
     */
    fun hasFeatureAccess(userId: String, feature: Feature): Boolean

    /**
     * Verifies if a user has specific permissions
     * @param userId The ID of the user
     * @param permissions The permissions to verify
     * @return true if user has all specified permissions, false otherwise
     */
    fun hasPermissions(userId: String, permissions: Set<Permission>): Boolean

    /**
     * Gets features accessible to a user
     * @param userId The ID of the user
     * @return Set of features the user can access
     */
    fun getAccessibleFeatures(userId: String): Set<Feature>

    /**
     * Handles access denial
     * @param userId The ID of the user
     * @param feature The feature that was denied
     * @return AccessDenial details
     */
    fun handleAccessDenial(userId: String, feature: Feature): AccessDenial
}