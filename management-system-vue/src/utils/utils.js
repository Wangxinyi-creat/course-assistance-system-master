/**
 * 深拷贝数组
 * @param input
 * @returns {{}|*|*[]}
 */
export function deepCopy(input) {
    if (typeof input !== 'object' || input === null) {
        // 如果是基本数据类型或者是 null，直接返回
        return input;
    }

    if (Array.isArray(input)) {
        // 如果是数组，递归拷贝每个元素
        const newArray = [];
        for (const item of input) {
            newArray.push(deepCopy(item));
        }
        return newArray;
    }

    // 如果是对象，递归拷贝每个属性
    const newObj = {};
    for (const key in input) {
        if (input.hasOwnProperty(key)) {
            newObj[key] = deepCopy(input[key]);
        }
    }
    return newObj;
}

/**
 * 递归筛选 菜单 树形结构数据 去除按钮的菜单数据
 * @param menuItems  树形数据
 * @returns {*}
 */
export function removeMenuType2(menuItems) {
    return menuItems.filter(item => item.menuType !== 2)
        .map(item => {
            if (item.children) {
                item.children = removeMenuType2(item.children).length > 0 ? removeMenuType2(item.children) : null;
            }
            return item;
        });
}
