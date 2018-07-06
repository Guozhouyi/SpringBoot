package com.inter.spring_boot.dao

import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.util.{Base64, Properties}

import com.google.gson.Gson
import com.inter.spring_boot.properties.Config
import com.inter.spring_boot.service.ConfigService
import javax.crypto.Cipher
import javax.crypto.spec.{IvParameterSpec, SecretKeySpec}
import org.apache.commons.crypto.cipher.CryptoCipher
import org.apache.commons.crypto.utils.Utils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
case class Token() {

  @Autowired
  var configService: ConfigService = _

  var username: String = _
  var password: String = _
  var env: String = _

  def this(username: String, password: String, env: String) {
    this()
    this.username = username
    this.password = password
    this.env = env
  }

  def getUTF8Bytes(input: String): Array[Byte] = {
    input.getBytes(StandardCharsets.UTF_8)
  }

  def asString(buffer: ByteBuffer): String = {
    val copy: ByteBuffer = buffer.duplicate()
    val bytes: Array[Byte] = new Array[Byte](copy.remaining())
    copy.get(bytes)
    new String(bytes, StandardCharsets.UTF_8)
  }

  @throws(classOf[Exception])
  def genAuthentication(username: String, password: String, env: String): String = {
    val token = new Token(username, password, env)

    val key: SecretKeySpec = new SecretKeySpec(getUTF8Bytes("1234567890123456"), "AES")
    val iv: IvParameterSpec = new IvParameterSpec(getUTF8Bytes("1234567890123456"))
    val properties: Properties = new Properties()
    val transform: String = "AES/CBC/PKCS5Padding"
    var outBuffer: ByteBuffer = null
    val bufferSize = 1024
    var updateBytes = 0
    var finalBytes = 0

    val gson: Gson = new Gson()
    val json: String = gson.toJson(token)
    var result: String = null

    if (json != null && !json.isEmpty) {
      var encipher: CryptoCipher = Utils.getCipherInstance(transform, properties)
      val inBuffer = ByteBuffer.allocateDirect(bufferSize)
      outBuffer = ByteBuffer.allocateDirect(bufferSize)
      inBuffer.put(getUTF8Bytes(json))
      inBuffer.flip()
      encipher.init(Cipher.ENCRYPT_MODE, key, iv)
      updateBytes = encipher.update(inBuffer, outBuffer)

      finalBytes = encipher.doFinal(inBuffer, outBuffer)

      outBuffer.flip()
      val encoded = new Array[Byte](updateBytes + finalBytes)
      outBuffer.duplicate().get(encoded)
      result = new String(Base64.getEncoder.encode(encoded))
    }
    result
  }

  @throws(classOf[Exception])
  def decodeToken(token: String): Token = {
    val key: SecretKeySpec = new SecretKeySpec(getUTF8Bytes("1234567890123456"), "AES")
    val iv: IvParameterSpec = new IvParameterSpec(getUTF8Bytes("1234567890123456"))
    val properties: Properties = new Properties()
    val transform: String = "AES/CBC/PKCS5Padding"
    val bufferSize = 1024

    val decipher: CryptoCipher = Utils.getCipherInstance(transform, properties)
    val outBuffer: ByteBuffer = ByteBuffer.allocateDirect(Base64.getDecoder.decode(token).length)
    outBuffer.put(Base64.getDecoder.decode(token))
    outBuffer.rewind()
    decipher.init(Cipher.DECRYPT_MODE, key, iv)
    val decoded: ByteBuffer = ByteBuffer.allocateDirect(bufferSize)
    decipher.update(outBuffer, decoded)
    decipher.doFinal(outBuffer, decoded)
    decoded.flip()
    val gson: Gson = new Gson()

    val tmp: Token = gson.fromJson(asString(decoded), classOf[Token])

    if (tmp != null && tmp.env.equals(configService.getEnv)) tmp
    else throw new IllegalArgumentException("illegal token!!")
  }
}
