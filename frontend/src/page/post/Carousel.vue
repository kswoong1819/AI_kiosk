<template>
  <div class="slider">
	<div class="slide-track">
		<div class="slide">
			<img src="@/assets/img/Architecture/AWS.png" height="80" alt="" />
		</div>
		<div class="slide">
			<img src="@/assets/img/Architecture/Bootstrap.png" height="80" alt="" />
		</div>
		<div class="slide">
			<img src="@/assets/img/Architecture/docker.png" height="80" alt="" />
		</div>
		<div class="slide">
			<img src="@/assets/img/Architecture/git.png" height="80" alt="" />
		</div>
		<div class="slide">
			<img src="@/assets/img/Architecture/java.png" height="80" alt="" />
		</div>
		<div class="slide">
			<img src="@/assets/img/Architecture/Jenkins.png" height="80" alt="" />
		</div>
		<div class="slide">
			<img src="@/assets/img/Architecture/jira.png" height="80" alt="" />
		</div>
		<div class="slide">
			<img src="@/assets/img/Architecture/maven.png" height="80" alt="" />
		</div>
		<div class="slide">
			<img src="@/assets/img/Architecture/N.png" height="80" alt="" />
		</div>
		<div class="slide">
			<img src="@/assets/img/Architecture/npm.png" height="80" alt="" />
		</div>
		<div class="slide">
			<img src="@/assets/img/Architecture/opencv.png" height="80" alt="" />
		</div>
		<div class="slide">
			<img src="@/assets/img/Architecture/python.png" height="80" alt="" />
		</div>
		<div class="slide">
			<img src="@/assets/img/Architecture/springboot.png" height="80" alt="" />
		</div>
		<div class="slide">
			<img src="@/assets/img/Architecture/tomcat.png" height="80" alt="" />
		</div>
		<div class="slide">
			<img src="@/assets/img/Architecture/Vue.png" height="80" alt="" />
		</div>
		<div class="slide">
			<img src="@/assets/img/Architecture/yarn.png" height="80" alt="" />
		</div>
	</div>
</div>
</template>

<script>
export default {

}
</script>

<style lang="scss" scope>

@mixin white-gradient {
	background: linear-gradient(to right,  rgba(255,255,255,1) 0%,rgba(255,255,255,0) 100%);
}

$animationSpeed: 40s;

// Animation
@keyframes scroll {
	0% { transform: translateX(0); }
	100% { transform: translateX(calc(-250px * 7))}
}


// Styling
.slider {
	height: 100px;
	margin: auto;
	overflow:hidden;
	position: relative;
	
	&::before,
	&::after {
		content: "";
		height: 100px;
		position: absolute;
		width: 200px;
		z-index: 2;
	}
	
	&::after {
		right: 0;
		top: 0;
		transform: rotateZ(180deg);
	}

	&::before {
		left: 0;
		top: 0;
	}
	
	.slide-track {
		animation: scroll $animationSpeed linear infinite;
		display: flex;
		width: calc(250px * 14);
	}
	
	.slide {
		height: 80px;
	}
    div>img {
        margin: 10px;
        margin-right: 30px;
    }
}
</style>