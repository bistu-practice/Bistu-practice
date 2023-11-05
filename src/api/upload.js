import request from './request'
export default {
  getTemAddress(tempUrlParams) {
    return request.post('/unifyCode/getUploadUrl', tempUrlParams)
  },
  tryTempUrl(tempUrl, file, head) {
    return request.put(tempUrl, file, head)
  },
  insertRes(insertParam) {
    return request.post('/unifyCode/decodeOneWithFile', insertParam)
  },
}
