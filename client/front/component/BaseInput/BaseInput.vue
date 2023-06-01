<template>
	<view class="base-input" :style="inputStyle">
		<slot name="prefix"/>
		<editor id="editor" class="ql-container" :placeholder="placeholder" @input="parseContentFun"
			 @ready="onEditorReady" @blur="clearFocus">
		</editor>
		<slot name="suffix"/>
	</view>
</template>

<script>

	export default {
		name: "BaseInput",
		props: {
			value: {
				type: String,
				default: ""
			},
			inputStyle: {
				type: Object,
				default: () => ({'width': '100%', 'height': '30px'})
			},
			emoji: {
				type: Object,
				default: () => ({})
			},
			placeholder: {
				type: String,
				default: ""
			}
		},
		watch: {
			emoji(val){
				if(!val || JSON.stringify(val) == "{}") return;
				if(!val.src || !val.name){
					throw "The emoji parameter is incomplete";
				}
				this.insertImg(val);
				this.$emit("update:emoji", {});
			},
			// 父组件所传过来的双向绑定的值，即v-model
			value(val) {
				if(val == ""){
					this.editorCtx.clear();
				}
			}
		},
		data() {
			return {};
		},
		created(){
			this.$bus.$on("recv", (res) => {
				if(res){
					this.editorCtx.insertText("");// 插入空字符串，获取焦点
				}
			})
		},
		methods: {
			onEditorReady() {
				uni.createSelectorQuery().select('#editor').context((res) => {
					this.editorCtx = res.context
				}).exec()
			},
			clearFocus(){
				setTimeout(() => {
					this.$bus.$emit("clearFocus", true);
				}, 500);
			},
			insertImg(emoji) {
				this.editorCtx.insertImage({
					src: emoji.src,
					width: '20px',
					height: '20px',
					extClass: 'emoji',
					data: {
						name: emoji.regName
					}
				})
			},
			parseContentFun(e) {
				this.getContent();
			},
			getContent() {
				this.editorCtx.getContents({
					success: (res) => {
						this.$emit("input", this.parseHtml(res.html))
					}
				})
			},
			/**
			 * 解析富文本内容
			 * @param {Object} html 富文本内容
			 */
			parseHtml(html) {
				// 1、对img表情进行解析，解析出表情对应的name
				let reg = /<img[\S\s]*?>/g;
				let imgList = html.match(reg);
				html = this.parseEmojiName(html, imgList);
				// 2、解析用户手动输入的标签文本
				html = this.parseInTab(html);
				// 3、解析富文本自带标签p
				html = this.parseDefaultTab(html);
				return html;
			},
			parseEmojiName(html, emojiList) {
				try{
					for (const imgStr of emojiList) {
						let name = imgStr.match(/name=[\S\s]*\]/)[0];
						name = name.replace("name=", "");
						html = html.replace(imgStr, name);
					}
				} catch {}
				return html;
			},
			/**
			 * 对用户输入的标签文本进行解析
			 * @param {Object} html
			 */
			parseInTab(html) {
				html = html.replace("&lt;", "<");
				html = html.replace("&gt;", ">");
				return html;
			},
			/**
			 * 解析富文本编辑器默认标签，p标签
			 * @param {Object} html
			 */
			parseDefaultTab(html) {
				let reg = new RegExp("<p>", 'g');
				let reg1 = new RegExp("</p>", 'g');
				let reg2 = new RegExp("<br?>", 'g');
				html = html.replace(reg, "");
				html = html.replace(reg1, "\n");
				html = html.replace(reg2, "");
				html = html.substring(0, html.lastIndexOf("\n"));
				return html;
			}
		},
	};
</script>

<style lang='scss' scoped>
	.base-input {
		display: flex;
		align-items: center;
		padding: 5px 0;

		::v-deep p {
			min-height: 25px;

			img{
				vertical-align: middle;
			}
		}

		.ql-container {
			display: block;
			height: 100%;
			min-height: 100%;
			padding: 0 5px;
			margin-right: 5px;
			border: 1px solid #ececec;
			outline: none;


			::v-deep .ql-blank:before {
				font-style: initial;
				top: 40%;
				transform: translateY(-50%);
			}
		}
	}
</style>
