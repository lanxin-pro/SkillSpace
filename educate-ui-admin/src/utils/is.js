// copy to vben-admin

const toString = Object.prototype.toString

export const is = (val, type) => {
  return toString.call(val) === `[object ${type}]`
}

export const isNumber = (val) => {
  return is(val, 'Number')
}

export const isString = (val) => {
  return is(val, 'String')
}

export const isUrl = (path) => {
  const reg =
      /(((^https?:(?:\/\/)?)(?:[-:&=\+\$,\w]+@)?[A-Za-z0-9.-]+(?::\d+)?|(?:www.|[-:&=\+\$,\w]+@)[A-Za-z0-9.-]+)((?:\/[\+~%\/.\w-_]*)?\??(?:[-\+=&%@.\w_]*)#?(?:[\w]*))?)$/
  return reg.test(path)
}
