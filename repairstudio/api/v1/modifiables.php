<?php 

require_once('C:\xampp\htdocs\repairstudio\api\include\dbconnect.php');
require_once 'C:\xampp\htdocs\repairstudio\api\include\dbhandler.php';

$app->get('/api/v1/data', function ($request, $response, $next) {
    $db = new DbHandler();
    $jsonBody = $request->getBody();
    $data = json_decode($jsonBody, true);
    
    $tokenArray = $request->getHeader('Authorization');
    if (preg_match('/Bearer\s(\S+)/', $tokenArray[0], $matches)) {
            $token = $matches[1];
        }
    
    $userData = $db->getData($token);
    if($userData != null) {
        return $response->withStatus(200)
        ->withHeader('Content-Type', 'application/json')
        ->write(json_encode($userData));
    } else {
        $responseBody = array('status' => 'failed');
        return $response->withStatus(401)
        ->withHeader('Content-Type', 'application/json')
        ->write(json_encode($responseBody));
    }
});

$app->post('/api/v1/addcustomer', function ($request, $response, $next) {
    $db = new DbHandler();
    $jsonBody = $request->getBody();
    $data = json_decode($jsonBody, true);
    
    $tokenArray = $request->getHeader('Authorization');
    if (preg_match('/Bearer\s(\S+)/', $tokenArray[0], $matches)) {
        $token = $matches[1];
    }
    
    $email = $data['email'];
    $name = $data['name'];
    $phone = $data['phone'];
    
    if($db->newCustomer($token, $name, $email, $phone)) {
        $responseBody = array('status' => 'success');
        return $response->withStatus(200)
        ->withHeader('Content-Type', 'application/json')
        ->write(json_encode($responseBody));
    } else {
        $responseBody = array('status' => 'failed');
        return $response->withStatus(401)
        ->withHeader('Content-Type', 'application/json')
        ->write(json_encode($responseBody));
    }
});

$app->get('/api/v1/readcustomers', function ($request, $response, $next) {
    $db = new DbHandler();
    $jsonBody = $request->getBody();
    $data = json_decode($jsonBody, true);
    
    $tokenArray = $request->getHeader('Authorization');
    if (preg_match('/Bearer\s(\S+)/', $tokenArray[0], $matches)) {
        $token = $matches[1];
    }
    
    $customerData = $db->getCustomers($token);
    $allCustomers = array();
    if($customerData != null) {
        $customers = array();
        while($row = $customerData->fetch_assoc())
        {
            array_push($customers, $row);
        }
        return $response->withStatus(200)
        ->withHeader('Content-Type', 'application/json')
        ->write(json_encode($customers));
    } else {
        $responseBody = array('status' => 'failed');
        return $response->withStatus(401)
        ->withHeader('Content-Type', 'application/json')
        ->write(json_encode($responseBody));
    }
});