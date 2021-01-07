<?php

$response = array();

if (isset($_POST['user_id'])) {
  $userId = $_POST['user_id'];

  require_once __DIR__ .'/db_config.php';

  $baglanti = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

  if (!$baglanti) {
    die("Hatalı bağlantı: " .mysqli_connect_error());
  }

  $sqlsorgu = "SELECT * FROM saved WHERE user_id = '$userId'";
  $result=mysqli_query($baglanti,$sqlsorgu);

  if (mysqli_num_rows($result) > 0) {
    $response['saved'] = array();

    while ($row = mysqli_fetch_assoc($result)) {
      $saved = array();

      $saved["saved_id"] = $row["saved_id"];
      $saved["post_id"] = $row["post_id"];
      $saved["user_id"] = $row["user_id"];
     

      array_push($response["saved"], $saved);

    }
    $response["success"] = 1;
    echo json_encode($response);



  }else {
    $response["success"] = 2;
    echo json_encode($response);
  }

  mysqli_close($baglanti);

}else {
  $response["success"] = 0;
  $response["message"] = "Required field(s) is missing";
  echo json_encode($response);
  mysqli_close($baglanti);
}
mysqli_close($baglanti);

?>
