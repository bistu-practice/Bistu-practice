<template>
    <div id="handupdate">
        <div id="header">
            <b-navbar>
                <template #start>
                    <b-navbar-item v-for="(route, index) in headRouters" :key="index" @click="toRoute(route.link)"
                        :class="{ actived: route.link == currentPath }">
                        {{ route.text }}
                    </b-navbar-item>
                </template>
            </b-navbar>
        </div>
        <div id="contain">
            <router-view></router-view>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            currentPath: '',
            headRouters: [
                {
                    link: '/handupdate/chooseupdate',
                    text: '地理信息上报',
                },
                {
                    link: '/handupdate/codeupdate',
                    text: '编码信息上报',
                },
            ]
        }
    },
    methods: {
        toRoute(link) {
            if (this.$route.fullPath == link) return
            this.$router.push(link)
        }
    },
    watch: {
        $route: {
            handler(route) {
                this.currentPath = route.fullPath
            },
            immediate: true
        }
    }
}
</script>

<style lang="less" scoped>
.actived {
    background-color: orange;
    color: black;
}

.navbar-item {
    margin-top: .3125rem;
    height: 2.1875rem;
    // box-sizing: border-box;
    border-top-left-radius: .3125rem;
    border-top-right-radius: .3125rem;
    border-top: .0625rem solid black;
    border-left: .0625rem solid black;
    border-right: .0625rem solid black;
}

/deep/.navbar {
    min-height: 2.5rem;
    height: 2.5rem;
    border-bottom: 1px solid grey;
}

/deep/.navbar-menu {
    height: 2.5rem;
}

/deep/.navbar-item:hover {
    // background-color: rgb(250, 188, 72);
    // color: black;
    color: orange;
    background-color: black;
}

#handupdate {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-content: center;
}

#header {
    height: 2.5rem;
    width: 100%;
    flex: 0 0 auto;
}

#contain {
    width: 100%;
    display: flex;
    flex-direction: row;
    flex: 1 0 auto;
}
</style>
