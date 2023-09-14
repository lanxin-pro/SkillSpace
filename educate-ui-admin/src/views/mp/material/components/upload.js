import { UploadType } from '@/utils/constants.js'
import { getAccessToken } from '@/utils/auth'

// 请求头
const HEADERS = { Authorization: 'Bearer ' + getAccessToken() }
// 上传地址
const UPLOAD_URL = import.meta.env.VITE_SERVER_BASE_API + '/admin-api/mp/material/upload-permanent'


const useBeforeUpload = (type, maxSizeMB) => {
    const fn = (rawFile) => {
        let allowTypes = []
        let name = ''

        switch (type) {
            case UploadType.Image:
                allowTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/bmp', 'image/jpg']
                maxSizeMB = 2
                name = '图片'
                break
            case UploadType.Voice:
                allowTypes = ['audio/mp3', 'audio/mpeg', 'audio/wma', 'audio/wav', 'audio/amr']
                maxSizeMB = 2
                name = '语音'
                break
            case UploadType.Video:
                allowTypes = ['video/mp4']
                maxSizeMB = 10
                name = '视频'
                break
        }
        // 格式不正确
        if (!allowTypes.includes(rawFile.type)) {
            message.error(`上传${name}格式不对!`)
            return false
        }
        // 大小不正确
        if (rawFile.size / 1024 / 1024 > maxSizeMB) {
            message.error(`上传${name}大小不能超过${maxSizeMB}M!`)
            return false
        }

        return true
    }

    return fn
}

const beforeImageUpload = (rawFile) => {
    console.log("啊哈")
    useBeforeUpload(UploadType.Image, 2)(rawFile)
}

const beforeVoiceUpload = (rawFile) => {
    useBeforeUpload(UploadType.Voice, 2)(rawFile)
}

const beforeVideoUpload = (rawFile) => {
    useBeforeUpload(UploadType.Video, 10)(rawFile)
}

export {
    HEADERS,
    UPLOAD_URL,
    UploadType,
    beforeImageUpload,
    beforeVoiceUpload,
    beforeVideoUpload
}
