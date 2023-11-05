import request from './request'
export default {
  getInfo(params) {
    return request.get('/unifyCode/getInfo', { params })
  },
  postExcel(yyfnb) {
    return request.post('/unifyCode/insertByExcel', yyfnb)
  },
  deleteLine(id) {
    return request.delete('/unifyCode/deleteOneCode?id=' + id)
  },
  decodeOneInsert(data) {
    return request.put('/unifyCode/decodeOneInsert', data)
  },
  getStatistics() {
    return request.get('/unifyCode/getStatistics')
  },
  getDownloadUrl(filename) {
    return request.post('/unifyCode/getDownloadUrl', filename)
  },
}
