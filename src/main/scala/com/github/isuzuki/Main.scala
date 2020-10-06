package com.github.isuzuki

import java.util.Properties

import org.postgresql.Driver

object Main extends App {
  val properties = new Properties()
  properties.put("user", "example")
  properties.put("password", "example")

  // @see https://jdbc.postgresql.org/documentation/head/connect.html#connection-parameters
  // 設定しない場合に、下記エラーが発生するケースがある
  // ERROR: column "time" is of type timestamp without time zone but expression is of type character varying
  properties.put("stringtype", "unspecified")

  val conn = new Driver().connect("jdbc:postgresql://localhost:5432/example", properties)

  val prepareInsertSql = "insert into item values ('id1', 'name1', ?)"
  // どちらでも問題なし
  val st = conn.prepareStatement(prepareInsertSql)
  st.setString(1, "2020-10-06 12:18:53.685929")
  st.execute()

  // stringtype = unspecifiedにしないと、エラー
  val st2 = conn.prepareStatement(prepareInsertSql)
  st2.setString(1, null)
  st2.execute()

  // stringtype = unspecifiedにしないと、エラー
  val st3 = conn.prepareStatement(prepareInsertSql)
  st3.setNull(1, java.sql.Types.NULL)
  st3.execute()

  // どちらでも問題なし
  val st4 = conn.prepareStatement(prepareInsertSql)
  st4.setTimestamp(1, null)
  st4.execute()
}
