<template>
  <div class="chat-app">
    <!-- 侧边栏 -->
    <div class="sidebar">
      <button class="new-chat-button" @click="startNewChat">+ 新对话</button>
      <div class="history-list">
        <!-- 历史对话倒序排列 -->
        <div
            v-for="(chat, index) in chatHistory.slice().reverse()"
            :key="index"
            class="history-item"
            @click="loadChat(chatHistory.length - 1 - index)"
        >
          <div class="history-title">对话 {{ chatHistory.length - index }}</div>
          <div class="history-preview">{{ chat[chat.length - 1]?.text || '无内容' }}</div>
        </div>
      </div>
    </div>

    <!-- 聊天主区域 -->
    <div class="chat-container">
      <!-- 头部区域 -->
      <div class="header">
        <h1>予礼</h1>
      </div>

      <!-- 消息区域 -->
      <div class="messages" ref="messagesContainer">
        <div v-for="(message, index) in messages" :key="index" class="message" :class="message.sender">
          <div class="content">
            <div v-if="message.sender === 'assistant' && message.rag" class="rag-tag">知识增强</div>
            <div class="text">{{ message.text }}</div>
            <div v-if="message.loading" class="typing-indicator">
              <span></span><span></span><span></span>
            </div>
            <div v-if="message.sender === 'assistant' && message.rag" class="rag-info">
              <i class="rag-icon">📚</i> 本次回答使用了知识库增强
            </div>
          </div>
        </div>
      </div>

      <!-- 输入区域 -->
      <div class="input-area">
        <!-- RAG控制区域单独一行 -->
        <div class="rag-control-container">
          <div class="rag-control">
            <label class="switch">
              <input type="checkbox" v-model="useRAG">
              <span class="slider"></span>
            </label>
            <div class="rag-label">
              <span class="rag-indicator" :class="{ active: useRAG }"></span>
              RAG知识增强 {{ useRAG ? '开启' : '关闭' }}
            </div>
            <div class="rag-tooltip">
              <i class="info-icon">i</i>
              <span class="tooltip-text">RAG功能开启时，系统会使用知识库增强回答准确性</span>
            </div>
          </div>
        </div>

        <!-- 输入框和发送按钮区域 -->
        <div class="input-message-container">
          <textarea
              v-model="inputMessage"
              placeholder="输入消息..."
              @keydown.enter.exact.prevent="sendMessage"
              ref="textarea"
          ></textarea>
          <button @click="sendMessage" :disabled="!inputMessage.trim() || loading">
            <svg v-if="!loading" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <line x1="22" y1="2" x2="11" y2="13"></line>
              <polygon points="22 2 15 22 11 13 2 9 22 2"></polygon>
            </svg>
            <span v-else class="loader"></span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ChatPage',
  data() {
    return {
      messages: [
        {
          sender: 'assistant',
          text: '您好！我是AI助手，很高兴为您服务。请问有什么可以帮您的？',
          rag: false
        }
      ],
      inputMessage: '',
      useRAG: true,
      loading: false,
      chatHistory: [
        [
          {
            sender: 'user',
            text: '什么是人工智能？',
            rag: false
          },
          {
            sender: 'assistant',
            text: '关于"什么是人工智能？"，这是一个很好的问题。一般来说，人工智能是指让计算机系统能够执行通常需要人类智能才能完成的任务，如学习、推理、解决问题等。',
            rag: false
          }
        ],
        [
          {
            sender: 'user',
            text: '人工智能有哪些应用领域？',
            rag: true
          },
          {
            sender: 'assistant',
            text: '根据知识库中的信息，人工智能有哪些应用领域？的相关内容如下：知识库增强提供了更准确的回答。人工智能的应用领域包括医疗保健、金融、交通、教育等多个领域。',
            rag: true
          }
        ]
      ],
      currentChatIndex: null
    };
  },
  methods: {
    sendMessage() {
      if (!this.inputMessage.trim() || this.loading) return;
      const userMessage = this.inputMessage.trim();
      this.inputMessage = '';

      // 添加用户消息
      this.messages.push({
        sender: 'user',
        text: userMessage
      });

      // 添加加载中的AI消息
      this.messages.push({
        sender: 'assistant',
        text: '',
        loading: true,
        rag: this.useRAG
      });

      this.loading = true;

      // 模拟API调用延迟
      setTimeout(() => {
        this.loading = false;

        // 移除加载状态的消息
        this.messages.pop();

        // 添加AI回复
        const ragEnabled = this.useRAG;
        this.messages.push({
          sender: 'assistant',
          text: this.generateResponse(userMessage, ragEnabled),
          rag: ragEnabled
        });

        // 滚动到底部
        this.$nextTick(() => {
          this.scrollToBottom();
        });
      }, 1500);
    },
    generateResponse(userMessage, ragEnabled) {
      const ragResponses = [
        `根据知识库中的信息，${userMessage}的相关内容如下：知识库增强提供了更准确的回答。在相关文献中，这个问题有详细讨论，结论是当前技术已经能够有效解决此类问题。`,
        `基于知识库分析，${userMessage}涉及多个领域。主要观点包括：1. 技术实现路径明确 2. 应用场景广泛 3. 未来发展潜力巨大。建议结合具体案例进一步探讨。`,
        `知识库检索显示，${userMessage}的核心要点是：创新解决方案已在多个行业应用，效果显著。根据最新研究报告，该方向的市场规模预计在未来三年增长300%。`
      ];
      const normalResponses = [
        `关于"${userMessage}"，这是一个很好的问题。一般来说，这取决于具体情况，但常见解决方案包括几个关键步骤。`,
        `我理解您的问题是：${userMessage}。根据我的分析，主要有三种方法可以解决这个问题，每种都有其优缺点。`,
        `对于"${userMessage}"，目前行业中有多种实践方法。最常用的是综合应用多种技术来达到理想效果。`
      ];
      return ragEnabled
          ? ragResponses[Math.floor(Math.random() * ragResponses.length)]
          : normalResponses[Math.floor(Math.random() * normalResponses.length)];
    },
    scrollToBottom() {
      const container = this.$refs.messagesContainer;
      container.scrollTop = container.scrollHeight;
    },
    adjustTextareaHeight() {
      const textarea = this.$refs.textarea;
      textarea.style.height = 'auto';
      textarea.style.height = `${Math.min(textarea.scrollHeight, 150)}px`;
    },
    startNewChat() {
      if (this.messages.length > 1) {
        this.chatHistory.push([...this.messages]);
      }
      this.messages = [
        {
          sender: 'assistant',
          text: '您好！我是AI助手，很高兴为您服务。请问有什么可以帮您的？',
          rag: false
        }
      ];
      this.currentChatIndex = null;
      this.$nextTick(() => {
        this.scrollToBottom();
      });
    },
    loadChat(index) {
      this.messages = [...this.chatHistory[index]];
      this.currentChatIndex = index;
      this.$nextTick(() => {
        this.scrollToBottom();
      });
    }
  },
  watch: {
    inputMessage() {
      this.$nextTick(this.adjustTextareaHeight);
    }
  },
  mounted() {
    this.scrollToBottom();
  }
};
</script>

<style scoped>
.chat-app {
  display: flex;
  height: 680px;
  max-width: 900px;
  margin: 0 auto;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
  position: relative;
  overflow: hidden;
  border-radius: 15px;
}

.sidebar {
  width: 120px;
  background: rgba(193, 161, 253, 0.72);
  border-right: 1px solid #e5e7eb;
  padding: 15px;
  display: flex;
  flex-direction: column;
}

.new-chat-button {
  background-color: rgb(255, 255, 255);
  color: #2c2c2c;
  border: none;
  padding: 10px;
  border-radius: 8px;
  cursor: pointer;
  margin-bottom: 15px;
}

.history-list {
  flex: 1;
  overflow-y: auto;
}

.history-item {
  padding: 10px;
  border-radius: 8px;
  cursor: pointer;
  margin-bottom: 8px;
}

.history-item:hover {
  background-color: #e5e7eb;
}

.history-title {
  font-weight: 600;
  margin-bottom: 4px;
}

.history-preview {
  font-size: 0.8rem;
  color: #6b7280;
}

.chat-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: rgba(241, 230, 255, 0.8);
}

.header {
  background: linear-gradient(135deg, rgba(193, 161, 253, 0.69) 0%, rgb(165, 126, 250) 100%);
  color: white;
  padding: 20px;
  text-align: center;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  z-index: 10;
}

.header h1 {
  margin: 0;
  font-size: 1.5rem;
  font-weight: 600;
}

/* 输入区域样式 */
.input-area {
  padding: 15px 20px;
  background-color: white;
  border-top: 1px solid #e5e7eb;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.05);
  position: relative;
}

/* RAG控制区域样式 - 单独一行 */
.rag-control-container {
  margin-bottom: 10px;
  padding: 8px 15px;
  background-color: rgba(165, 126, 250, 0.05);
  border-radius: 16px;
}

.rag-control {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 12px;
}

.switch {
  position: relative;
  display: inline-block;
  width: 50px;
  height: 24px;
}

.switch input {
  opacity: 0;
  width: 0;
  height: 0;
}

.slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ccc;
  transition: .4s;
  border-radius: 24px;
}

.slider:before {
  position: absolute;
  content: "";
  height: 18px;
  width: 18px;
  left: 3px;
  bottom: 3px;
  background-color: white;
  transition: .4s;
  border-radius: 50%;
}

input:checked + .slider {
  background-color: #10b981;
}

input:checked + .slider:before {
  transform: translateX(26px);
}

.rag-label {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.85rem;
  font-weight: 500;
  color: #374151;
}

.rag-indicator {
  display: inline-block;
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background-color: #ccc;
}

.rag-indicator.active {
  background-color: #10b981;
  box-shadow: 0 0 8px #10b981;
}

.rag-tooltip {
  position: relative;
  display: inline-flex;
  align-items: center;
}

.info-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 18px;
  height: 18px;
  border-radius: 50%;
  background-color: rgba(165, 126, 250, 0.2);
  font-size: 0.6rem;
  font-weight: bold;
  cursor: help;
  color: rgba(165, 126, 250, 0.8);
}

.tooltip-text {
  visibility: hidden;
  width: 200px;
  background-color: rgba(0, 0, 0, 0.8);
  color: #fff;
  text-align: center;
  border-radius: 6px;
  padding: 8px;
  position: absolute;
  z-index: 1;
  bottom: 125%;
  left: 50%;
  transform: translateX(-50%);
  opacity: 0;
  transition: opacity 0.3s;
  font-size: 0.7rem;
  font-weight: normal;
}

.rag-tooltip:hover .tooltip-text {
  visibility: visible;
  opacity: 1;
}

/* 输入框和发送按钮区域样式 */
.input-message-container {
  display: flex;
  align-items: center;
  border: 1px solid rgba(165, 126, 250, 0.5);
  border-radius: 24px;
  padding: 8px 15px;
  background-color: white;
  transition: border-color 0.3s;
}

.input-message-container:focus-within {
  border-color: rgba(165, 126, 250, 0.8);
  box-shadow: 0 0 0 3px rgba(165, 126, 250, 0.2);
}

textarea {
  flex: 1;
  border: none;
  resize: none;
  outline: none;
  font-size: 0.9rem;
  line-height: 1.5;
  padding: 10px 0;
  max-height: 150px;
  overflow-y: auto;
  font-family: inherit;
  background-color: transparent;
}

button {
  background: none;
  border: none;
  cursor: pointer;
  color: rgba(165, 126, 250, 0.8);
  padding: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: all 0.2s;
  margin-left: 10px;
}

button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

button:not(:disabled):hover {
  background-color: rgba(165, 126, 250, 0.1);
  transform: scale(1.05);
}

button svg {
  width: 22px;
  height: 22px;
}

.messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 20px;
  background-color: #f5f7fb;
}

.message {
  display: flex;
  max-width: 85%;
  animation: fadeIn 0.3s ease-out;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.message.user {
  align-self: flex-end;
}

.message.assistant {
  align-self: flex-start;
}

.content {
  flex: 1;
  min-width: 0;
}

.rag-tag {
  display: inline-block;
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: white;
  font-size: 0.6rem;
  padding: 2px 8px;
  border-radius: 12px;
  margin-bottom: 5px;
}

.text {
  padding: 12px 16px;
  border-radius: 18px;
  font-size: 0.9rem;
  line-height: 1.5;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
}

.message.user .text {
  background: linear-gradient(135deg, rgba(165, 126, 250, 0.8) 0%, rgba(155, 112, 248, 0.94) 100%);
  color: white;
  border-bottom-right-radius: 5px;
}

.message.assistant .text {
  background-color: white;
  color: #1f2937;
  border: 1px solid #e5e7eb;
  border-bottom-left-radius: 5px;
}

.typing-indicator {
  display: flex;
  padding: 10px 0;
}

.typing-indicator span {
  display: inline-block;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background-color: #9ca3af;
  margin: 0 3px;
  animation: bounce 1.5s infinite ease-in-out both;
}

.typing-indicator span:nth-child(1) {
  animation-delay: -0.32s;
}

.typing-indicator span:nth-child(2) {
  animation-delay: -0.16s;
}

@keyframes bounce {
  0%, 80%, 100% { transform: scale(0); }
  40% { transform: scale(1.0); }
}

.rag-info {
  font-size: 0.7rem;
  color: #6b7280;
  margin-top: 8px;
  display: flex;
  align-items: center;
  gap: 5px;
}

.loader {
  width: 20px;
  height: 20px;
  border: 3px solid rgba(165, 126, 250, 0.2);
  border-top: 3px solid rgba(165, 126, 250, 0.8);
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>