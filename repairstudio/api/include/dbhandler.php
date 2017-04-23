<?php
 
class DbHandler {
 
    private $conn;
 
    function __construct() {
        require_once dirname(__FILE__) . './DbConnect.php';
        // opening db connection
        $db = new DbConnect();
        $this->conn = $db->connect();
    }

    public function register($email, $password, $name) {
        if (!$this->doesUserExist($email)) {
            
            $password_hash = password_hash($password, PASSWORD_DEFAULT);
            $stmt = $this->conn->prepare("INSERT INTO users (email, password, name, created, membership) VALUES(?,?,?,NOW(), 0)");
            $stmt->bind_param("sss", $email, $password_hash, $name);
            $result = $stmt->execute();
            $stmt->close();
 
            if ($result) {
                return TRUE;
            } else {
                return FALSE;
            }
        } else {
            return FALSE;
        }
    }
    
    public function login($email, $password) {
        if ($this->doesUserExist($email)) {
            $password_hash = password_hash($password, PASSWORD_DEFAULT);
            $stmt = $this->conn->prepare("SELECT password FROM users WHERE email=?");
            $stmt->bind_param("s", $email);
            $result = $stmt->execute();
            if ($result) {
                $password_hash = $stmt->get_result()->fetch_assoc()['password'];
                $stmt->close();
                if(password_verify($password, $password_hash))
                {
                    return TRUE;
                } else {
                    return FALSE;
                }
            } else {
                return FALSE;
            }
        } else {
            return FALSE;
        }
    }
    
    public function getData($token) {
        $stmt = $this->conn->prepare("SELECT `user` FROM `tokens` WHERE token=?");
        $stmt->bind_param("s", $token);
        $result = $stmt->execute();
        if ($result) {
            $id = $stmt->get_result()->fetch_assoc()['user'];
            $stmt->close();
            
            $stmt = $this->conn->prepare("SELECT `email`,`name`,`membership` FROM `users` WHERE id=?");
            $stmt->bind_param("s", $id);
            $stmt->execute();
            $data = $stmt->get_result()->fetch_assoc();
            return $data;
        } else {
            return null;
        }
    }
    
    private function doesUserExist($email) {
        $stmt = $this->conn->prepare("SELECT email FROM `users` WHERE email=?");
        $stmt->bind_param("s", $email);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        if ($num_rows > 0) {
            return TRUE;
        } else {
            return FALSE;
        }
    }
    
    public function generateAuthToken($email) {
        $token = bin2hex(openssl_random_pseudo_bytes(16));
        
        $stmt = $this->conn->prepare("SELECT id FROM users WHERE email=?");
        $stmt->bind_param("s", $email);
        $result = $stmt->execute();
        
        if ($result) {
            $id = $stmt->get_result()->fetch_assoc()['id'];
            $stmt->close();
            
            $stmt = $this->conn->prepare("SELECT user FROM tokens WHERE user=?");
            $stmt->bind_param("s", $id);
            $stmt->execute();
            $stmt->store_result();
            $num_rows = $stmt->num_rows;
            
            if ($num_rows > 0) {
                $stmt = $this->conn->prepare("UPDATE tokens SET token=? WHERE user=?");
                $stmt->bind_param("ss", $token, $id);
                $stmt->execute();
                return $token;
            
            } else {
                $stmt = $this->conn->prepare("INSERT INTO tokens (user, token, expires) VALUE(?,?,NOW() + INTERVAL 30 DAY)");
                $stmt->bind_param("ss", $id, $token);
                $stmt->execute();
                return $token;
            }
        }
    }
    
    private function getUserFromToken($token) {
        $stmt = $this->conn->prepare("SELECT `user` FROM `tokens` WHERE `token`=?");
        $stmt->bind_param("s", $token);
        $result = $stmt->execute();
        if ($result) {
            $user = $stmt->get_result()->fetch_assoc()['user'];
            $stmt->close();
            return $user;
        }
    }
    
    public function newCustomer($token, $name, $email, $phone) {
        $user = $this->getUserFromToken($token);
        if ($user != null) {
            $stmt = $this->conn->prepare("INSERT INTO `customers` (`user`,`name`,`email`,`phone`) VALUES(?,?,?,?)");
            $stmt->bind_param("ssss", $user, $name, $email, $phone);
            $result = $stmt->execute();
            $stmt->close();
 
            if ($result) {
                return TRUE;
            } else {
                return FALSE;
            }
        } else {
            return FALSE;
        }
    }
    
    public function getCustomers($token) {
        $user = $this->getUserFromToken($token);
        if($user != null) {
            $stmt = $this->conn->prepare("SELECT * FROM `customers` WHERE `user`=?");
            $stmt->bind_param("s", $user);
            $result = $stmt->execute();
            if($result)
            {
                $data = $stmt->get_result();
                $stmt->close();
                return $data;
            }
        } else {
            return null;
        }
    }
    
    public function authenticate($token)
    {
        $stmt = $this->conn->prepare("SELECT `user` FROM `tokens` WHERE `token`=?");
        $stmt->bind_param("s", $token);
        $result = $stmt->execute();
        if ($result) 
        {
            $user = $stmt->get_result()->fetch_assoc()['user'];
            $stmt->close();
            return $user;
        }
        else
        {
            return null;
        }
    }
}