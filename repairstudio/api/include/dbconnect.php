<?php
class DbConnect {
 
    private $conn;
 
    function __construct() {        
    }
 
    function connect() {
        include_once dirname(__FILE__) . './Config.php';
 
        $this->conn = new mysqli(DB_HOST, DB_USERNAME, DB_PASSWORD, DB_NAME);
 
        // ---------------- delete before production 
        if (mysqli_connect_errno()) {
            echo "Failed to connect to MySQL: " . mysqli_connect_error();
        }
        // delete before production ---------------------
        return $this->conn;
    }
}
?>