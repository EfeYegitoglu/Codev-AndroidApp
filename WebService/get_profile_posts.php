<?php

$response = array();

if (isset($_POST['user_id'])) {
  $userId = $_POST['user_id'];

  require_once __DIR__ .'/db_config.php';

  $baglanti = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

  if (!$baglanti) {
    die("Hatalı bağlantı: " .mysqli_connect_error());
  }

  $sqlsorgu = "SELECT * FROM posts WHERE user_id = '$userId'";
  $result=mysqli_query($baglanti,$sqlsorgu);

  if (mysqli_num_rows($result) > 0) {
    $response['posts'] = array();

    while ($row = mysqli_fetch_assoc($result)) {
      $posts = array();

      $posts["post_id"] = $row["post_id"];
      $posts["user_id"] = $row["user_id"];
      $posts["post_picture_one"] = $row["post_picture_one"];
      $posts["post_picture_url_one"] = $row["post_picture_url_one"];
      $posts["post_picture_two"] = $row["post_picture_two"];
      $posts["post_picture_url_two"] = $row["post_picture_url_two"];
      $posts["post_picture_three"] = $row["post_picture_three"];
      $posts["post_picture_url_three"] = $row["post_picture_url_three"];
      $posts["post_title"] = $row["post_title"];
      $posts["post_explanation"] = $row["post_explanation"];
      $posts["post_category"] = $row["post_category"];

      array_push($response["posts"], $posts);

    }
    $response["success"] = 1;
    $response["message"] = "Başarılı";
    echo json_encode($response);



  }else {
    $response["success"] = 2;
    $response["message"] = "Başarısız";
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
