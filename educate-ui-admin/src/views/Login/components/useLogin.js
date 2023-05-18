import { ref,computed } from 'vue'


// 枚举类型，如果使用ts的话将会很方便
export const LoginStateEnum = Object.freeze({
    LOGIN: 'LOGIN',
    REGISTER: 'REGISTER',
    RESET_PASSWORD: 'RESET_PASSWORD',
    MOBILE: 'MOBILE',
    QR_CODE: 'QR_CODE',
    SSO: 'SSO'
})

// 当前的点击的登录方式默认为LOGIN
const currentState = ref(LoginStateEnum.LOGIN)

export const useLoginState = (()=>{
    const getLoginState = computed(() => currentState.value)

    const setLoginState = ((setLoginState)=>{
        currentState.value = setLoginState
    })

    const handleBackLogin = (()=>{
        setLoginState(LoginStateEnum.LOGIN)
    })
    return {
        getLoginState,
        setLoginState,
        handleBackLogin
    }
})
