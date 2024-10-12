<template>
	<div class="h100">
		<transition :name="setTransitionName" mode="out-in">
      <router-view :key="refreshRouterViewKey" />
		</transition>
	</div>
</template>

<script>
export default {
	name: 'parent',
	data() {
		return {
			refreshRouterViewKey: null,
		};
	},
	created() {
		// 页面加载前，处理缓存，页面刷新时路由缓存处理
		this.bus.$on('onTagsViewRefreshRouterView', (path) => {
			if (this.$route.path !== path) return false;
			this.refreshRouterViewKey = this.$route.path;
			this.$nextTick(() => {
				this.refreshRouterViewKey = null;
			});
		});
	},
	computed: {
		// 设置主界面切换动画
		setTransitionName() {
			return this.$store.state.themeConfig.themeConfig.animation;
		},
	},
	methods: {},
};
</script>
