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
