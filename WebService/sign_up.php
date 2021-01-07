<?php

$response = array();

if(isset($_POST['user_mail']) && isset($_POST['user_password'])){

    $mailTo = $_POST['mailTo'];
    $user_mail = $_POST['user_mail'];
    $user_password = $_POST['user_password'];
    $verification_code = rand(0,10000) . rand(0,10000);
    $account_state = 0;
    
    require_once __DIR__ . '/db_config.php';

    $connect = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

    if(!connect){
        die("Hatalı bağlantı : " . mysqli_connect_error());
    }

    $control = mysqli_query($connect,"SELECT * FROM users WHERE user_mail = '$user_mail' ");
    $count = mysqli_num_rows($control);

    if($count > 0){
        $response["success"] = 0;
        $response["message"] = "Girmiş Olduğunuz Mail Zaten Mevcut";
        echo json_encode($response);
    }else{

        $sqlquery = "INSERT INTO users (user_mail,user_password,verification_code,account_state) 
            VALUES ('$user_mail','$user_password','$verification_code','$account_state')";

        if (mysqli_query($connect, $sqlquery)) {
            $response["success"] = 1;
            $response["message"] = "successfully ";
            echo json_encode($response);
            
            $to =$mailTo;
            $subject = "CoDev Software Hesap Aktiflestirme";
            $text = "Hesabınızı aktifleştirmek için dogrulama kodunu uygulamadaki gerekli alana yapıştırınız.\n\nDogrulama kodunuz: " ."$verification_code" ;
            $from = "From: codevsoftware@gmail.com";
            mail($to,$subject,$text,$from);
            
        } else {
            $response["success"] = 0;
            $response["message"] = "No product found";
            echo json_encode($response);
        }
        //bağlantı koparılır.
        mysqli_close($connect);

    }

}else{
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
    echo json_encode($response);
}
?>