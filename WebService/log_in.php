<?php

$response = array();

if(isset($_POST['user_mail']) && isset($_POST['user_password'])){
    $userMail = $_POST['user_mail'];
    $userPassword = $_POST['user_password'];

    require_once __DIR__ . '/db_config.php';

    $baglanti = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

    if(!$baglanti){die("Hatalı bağlantı : " . mysqli_connect_error());}

    $control = mysqli_query($baglanti,"SELECT * FROM users WHERE user_mail = '$userMail' and user_password = '$userPassword'");
    $count = mysqli_num_rows($control);

    if($count > 0){

        $sqlqueryfirst = "SELECT * FROM users WHERE user_mail = '$userMail' and user_password = '$userPassword' and account_state = '1'";
        
        $sqlquerysecond = "SELECT * FROM users WHERE user_mail = '$userMail' and user_password = '$userPassword'";

        if (mysqli_query($baglanti, $sqlqueryfirst)) {
            $response["success"] = 1;
            $response["message"] = "Giriş Yapıldı";
            echo json_encode($response);
            mysqli_close($baglanti);
        }

        
    }else{

        $response["success"] = 2;
        $response["message"] = "Girmiş Olduğunuz Bilgilere Ait Kullanıcı Bulunmamaktadır";
        echo json_encode($response);
        mysqli_close($baglanti);
    }
    


}else{
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
    echo json_encode($response);
}


?>