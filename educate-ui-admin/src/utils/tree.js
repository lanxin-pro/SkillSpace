export const defaultProps = {
    children: 'children',
    label: 'name',
    value: 'id',
    isLeaf: 'leaf'
}


/**
 * 构造树型结构数据
 * @param {*} data 数据源
 * @param {*} id id字段 默认 'id'
 * @param {*} parentId 父节点字段 默认 'parentId'
 * @param {*} children 孩子节点字段 默认 'children'
 */
export const handleTree = (data, id, parentId, children) => {
    if (!Array.isArray(data)) {
        console.warn('data must be an array')
        return []
    }
    const config = {
        id: id || 'id',
        parentId: parentId || 'parentId',
        childrenList: children || 'children'
    }

    const childrenListMap = {}
    const nodeIds = {}
    const tree = []

    for (const d of data) {
        const parentId = d[config.parentId]
        if (childrenListMap[parentId] == null) {
            childrenListMap[parentId] = []
        }
        nodeIds[d[config.id]] = d
        childrenListMap[parentId].push(d)
    }

    for (const d of data) {
        const parentId = d[config.parentId]
        if (nodeIds[parentId] == null) {
            tree.push(d)
        }
    }

    for (const t of tree) {
        adaptToChildrenList(t)
    }

    function adaptToChildrenList(o) {
        if (childrenListMap[o[config.id]] !== null) {
            o[config.childrenList] = childrenListMap[o[config.id]]
        }
        if (o[config.childrenList]) {
            for (const c of o[config.childrenList]) {
                adaptToChildrenList(c)
            }
        }
    }
    return tree
}



/**
 * 构造树型结构数据
 * @param {*} data 数据源
 * @param {*} id id字段 默认 'id'
 * @param {*} parentId 父节点字段 默认 'parentId'
 * @param {*} children 孩子节点字段 默认 'children'
 * @param {*} rootId 根Id 默认 0
 */
// @ts-ignore
export const handleTree2 = (data, id, parentId, children, rootId) => {
    id = id || 'id'
    parentId = parentId || 'parentId'
    // children = children || 'children'
    rootId =
        rootId ||
        Math.min(
            ...data.map((item) => {
                return item[parentId]
            })
        ) ||
        0
    // 对源数据深度克隆
    const cloneData = JSON.parse(JSON.stringify(data))
    // 循环所有项
    const treeData = cloneData.filter((father) => {
        const branchArr = cloneData.filter((child) => {
            // 返回每一项的子级数组
            return father[id] === child[parentId]
        })
        branchArr.length > 0 ? (father.children = branchArr) : ''
        // 返回第一层
        return father[parentId] === rootId
    })
    return treeData !== '' ? treeData : data
}
