import { ref,computed } from 'vue'


// 枚举类型，如果使用ts的话将会很方便
export const LoginStateEnum = Object.freeze({
    LOGIN: 'LOGIN',
    REGISTER: 'REGISTER',
    RESET_PASSWORD: 'RESET_PASSWORD',
    MOBILE: 'MOBILE',
    QR_CODE: 'QR_CODE',
    SSO_ACCREDIT: 'SSO_ACCREDIT',
    SOCIAL_ACCREDIT: 'SOCIAL_ACCREDIT'
})

// 当前的点击的登录方式默认为LOGIN
const currentState = ref(LoginStateEnum.LOGIN)
const socialType = ref()

/**
 * 改变Login页面的显示与隐藏
 * 如果想使用下面的方法需要导入并解构
 *
 * @type {function(): {getLoginState: ComputedRef<string>, handleBackLogin: function(): void, setLoginState: function(*): void}}
 */
export const useLoginState = (()=>{

    /**
     * 当前的一个状态
     * @type {ComputedRef<string>}
     */
    const getLoginState = computed(() => currentState.value)

    /**
     * 修改当前的一个状态
     * @type {setLoginState}
     */
    const setLoginState = ((setLoginState)=>{
        currentState.value = setLoginState
    })

    /**
     * 返回，上一级肯定是Login
     * @type {handleBackLogin}
     */
    const handleBackLogin = (()=>{
        setLoginState(LoginStateEnum.LOGIN)
    })

    return {
        getLoginState,
        setLoginState,
        handleBackLogin
    }
})
