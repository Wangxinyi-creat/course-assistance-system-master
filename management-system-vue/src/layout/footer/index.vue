<template>
	<div class="layout-footer mt15">
		<div class="layout-footer-warp">
			<div>后台管理系统，zhedian</div>
			<div class="mt5">版权所有：zhedian</div>
		</div>
	</div>
</template>

<script>
export default {
	name: 'layoutFooter',
	data() {
		return {};
	},
};
</script>

<style scoped lang="scss">
.layout-footer {
	width: 100%;
	display: flex;
	&-warp {
		margin: auto;
		color: var(--prev-color-text-secondary);
		text-align: center;
	}
}
</style>
