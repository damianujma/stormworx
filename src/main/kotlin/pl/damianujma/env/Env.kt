package pl.damianujma.env

private const val JDBC_URL: String = "jdbc:mariadb://localhost:3306/stormworx"
private const val JDBC_USER: String = "root"
private const val JDBC_PW: String = "stormworx"
private const val JDBC_DRIVER: String = "org.mariadb.jdbc.Driver"

data class Env(val dataSource: DataSource = DataSource()) {
    data class DataSource(
        val url: String = System.getenv("MARIADB_URL") ?: JDBC_URL,
        val username: String = System.getenv("MARIADB_USERNAME") ?: JDBC_USER,
        val password: String = System.getenv("MARIADB_PASSWORD") ?: JDBC_PW,
        val driver: String = JDBC_DRIVER,
    )
}