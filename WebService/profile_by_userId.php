<?php

$response = array();

if(isset($_POST['user'])){
    $userId = $_POST['user'];

    require_once __DIR__ . '/db_config.php';

    $baglanti = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

    if (!$baglanti) {
            die("Hatalı bağlantı : " . mysqli_connect_error());
        }

    $sqlsorgu = "SELECT * FROM profiles WHERE user = '$userId'";
    $result = mysqli_query($baglanti,$sqlsorgu);

    if(mysqli_num_rows($result) > 0){
        $response['profiles'] = array();

        while($row = mysqli_fetch_assoc($result)){

            $profiles = array();

            $profiles["profile_id"] = $row["profile_id"];
            $profiles["profile_picture"] = $row["profile_picture"];
            $profiles["profile_picture_url"] = $row["profile_picture_url"];
            $profiles["profile_name"] = $row["profile_name"];
            $profiles["profile_info"] = $row["profile_info"];
            $profiles["profile_website"] = $row["profile_website"];
            $profiles["user"] =$row["user"];

            array_push($response["profiles"], $profiles);
        }

        $response["success"] = 1;
        echo json_encode($response);
    }else{
        
        $response['profiles'] = array();

        while($row = mysqli_fetch_assoc($result)){

            $profiles = array();

            $profiles["profile_id"] = $row["profile_id"];
            $profiles["profile_picture"] = $row["profile_picture"];
            $profiles["profile_picture_url"] = $row["profile_picture_url"];
            $profiles["profile_name"] = $row["profile_name"];
            $profiles["profile_info"] = $row["profile_info"];
            $profiles["profile_website"] = $row["profile_website"];
            $profiles["user"] =$row["user"];

            array_push($response["profiles"], $profiles);
        }
        
        $response["success"] = 2;
        echo json_encode($response);
    }

    mysqli_close($baglanti);

}else {
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
    echo json_encode($response);
    
}

?>