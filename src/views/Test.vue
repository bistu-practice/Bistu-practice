<template>
  <div>
    <input type="file" @change="fileChange" />
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: 'Test',
  methods: {
    fileChange(e) {
      const file = e.target.files?.[0]
      if (!file) return
      console.log(file)
      this.uploadFile(file)
    },
    async uploadFile(file) {
      // 获得文件信息
      const fileName = file.name
      const contentType = file.type
      const code = '632626200206202105220204001010302001'
      // 请求临时地址
      const tempUrlParams = {
        code,
        fileName,
        contentType,
      }
      const tempUrlRes = await axios.post(
        '/api/unifyCode/getUploadUrl',
        tempUrlParams,
      )
      if (tempUrlRes.data.code != 0) {
        console.error(tempUrlRes)
        console.error('获得临时地址错误')
        return
      }
      console.log(tempUrlRes)
      const resData1 = tempUrlRes.data.data
      // 获得处理后的文件名，临时地址
      const finFileName = resData1.fileName
      const tempUrl = resData1.tempUrl
      const disposition = resData1.disposition
      const headers = {}
      headers['Content-Type'] = contentType
      headers['Content-Disposition'] = disposition
      let uploadFileRes
      // 调用阿里云上传文件接口
      try {
        uploadFileRes = await axios.put(tempUrl, file, {
          headers,
          withCredentials: false,
        })
      } catch (err) {
        console.error(err)
        console.error('阿里云上传文件错误')
      }
      console.log(uploadFileRes)
      // 向服务器正式发送插入请求
      const insertParam = {
        code,
        fileName: finFileName,
      }
      const insertRes = await axios.post(
        '/api/unifyCode/decodeOneWithFile',
        insertParam,
      )
      if (insertRes.data.code != 0) {
        console.error('插入数据错误')
      }
      console.log(insertRes)
    },
  },
  created() {},
}
</script>

<style lang="less" scoped></style>
