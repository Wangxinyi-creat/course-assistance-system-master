const routesListModule = {
    namespaced: true,
    state: {
        dictList: {},
    },
    mutations: {
        getDictList(state, data) {
            state.dictList = data;
        },
    },
    actions: {
        async setDictList({commit}, data) {
            commit('getDictList', data);
        },
    },
};

export default routesListModule;
