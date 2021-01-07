<?php

$response = array();

if (isset($_POST['post_id'])) {
  $postId = $_POST['post_id'];

  require_once __DIR__ .'/db_config.php';

  $baglanti = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

  if (!$baglanti) {
    die("Hatalı bağlantı: " .mysqli_connect_error());
  }

  $sqlsorgu = "SELECT * FROM comments WHERE post_id = '$postId' ORDER BY comment_id DESC";
  $result=mysqli_query($baglanti,$sqlsorgu);

  if (mysqli_num_rows($result) > 0) {
    $response['comments'] = array();

    while ($row = mysqli_fetch_assoc($result)) {
      $comments = array();

      $comments["comment_id"] = $row["comment_id"];
      $comments["post_id"] = $row["post_id"];
      $comments["comment_text"] = $row["comment_text"];
      $comments["user_id"] = $row["user_id"];


      array_push($response["comments"], $comments);

    }
    $response["success"] = 1;
    $response["message"] = "Başarılı";
    echo json_encode($response);


  }else {
    $response["success"] = 2;
    $response["message"] = "Bu gönseri için cevap bulunmamaktadır";
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
