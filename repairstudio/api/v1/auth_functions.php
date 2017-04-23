<?php 

require_once('C:\xampp\htdocs\repairstudio\api\include\dbconnect.php');
require_once 'C:\xampp\htdocs\repairstudio\api\include\dbhandler.php';

$app->post('/api/v1/login', function ($request, $response, $next) {
    $db = new DbHandler();
    $jsonBody = $request->getBody();
    $data = json_decode($jsonBody, true);
    
    $email = $data['email'];
    $password = $data['password'];
    
    if($db->login($email, $password)){
        $token = $db->generateAuthToken($email);
        $responseBody = array('token' => $token);
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

$app->post('/api/v1/register', function ($request, $response, $next) {
    $db = new DbHandler();
    $jsonBody = $request->getBody();
    $data = json_decode($jsonBody, true);
    
    $email = $data['email'];
    $password = $data['password'];
    $name = $data['name'];
    
    if($db->register($email, $password, $name)) {
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

$app->get('/api/v1/authenticate', function ($request, $response, $next) {
    $db = new DbHandler();
    $jsonBody = $request->getBody();
    $data = json_decode($jsonBody, true);
    
    $tokenArray = $request->getHeader('Authorization');
    if (preg_match('/Bearer\s(\S+)/', $tokenArray[0], $matches)) 
    {
        $token = $matches[1];
    }
    if($db->authenticate($token) != null) {
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