// Basit Singleton AuthService
class AuthService {
  static instance;

  constructor(api) {
    if (AuthService.instance) {
      return AuthService.instance;
    }
    this.api = api;
    AuthService.instance = this;
  }

  setToken(token) {
    if (!token) return;
    localStorage.setItem('authToken', token);
  }

  getToken() {
    return localStorage.getItem('authToken');
  }

  logout() {
    localStorage.removeItem('authToken');
  }

  async login(email, password) {
    const response = await this.api.login(email, password);
    this.setToken(response?.token);
    return response;
  }

  async register(payload) {
    const response = await this.api.register(payload);
    this.setToken(response?.token);
    return response;
  }
}

export default AuthService;

