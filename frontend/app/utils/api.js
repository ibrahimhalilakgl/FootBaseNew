import request from './request';
import { API_BASE_URL, API_ENDPOINTS } from './constants';

// Auth API
export const authAPI = {
  login: (email, password) =>
    request(`${API_BASE_URL}${API_ENDPOINTS.AUTH.LOGIN}`, {
      method: 'POST',
      body: JSON.stringify({ email, password }),
    }),

  register: (data) =>
    request(`${API_BASE_URL}${API_ENDPOINTS.AUTH.REGISTER}`, {
      method: 'POST',
      body: JSON.stringify(data),
    }),

  resetPassword: (email, newPassword) =>
    request(`${API_BASE_URL}${API_ENDPOINTS.AUTH.RESET_PASSWORD}`, {
      method: 'POST',
      body: JSON.stringify({ email, newPassword }),
    }),
};

// Matches API
export const matchesAPI = {
  list: (params = {}) => {
    const queryString = new URLSearchParams(params).toString();
    const url = `${API_BASE_URL}${API_ENDPOINTS.MATCHES.LIST}${queryString ? `?${queryString}` : ''}`;
    return request(url);
  },

  get: (id) => request(`${API_BASE_URL}${API_ENDPOINTS.MATCHES.GET(id)}`),

  predict: (matchId, homeScore, awayScore) =>
    request(`${API_BASE_URL}${API_ENDPOINTS.MATCHES.PREDICT(matchId)}`, {
      method: 'POST',
      body: JSON.stringify({ homeScore, awayScore }),
    }),

  addComment: (matchId, message) =>
    request(`${API_BASE_URL}${API_ENDPOINTS.MATCHES.COMMENT(matchId)}`, {
      method: 'POST',
      body: JSON.stringify({ message }),
    }),

  updateComment: (commentId, message) =>
    request(`${API_BASE_URL}${API_ENDPOINTS.MATCHES.DELETE_COMMENT(commentId)}`, {
      method: 'PUT',
      body: JSON.stringify({ message }),
    }),

  deleteComment: (commentId) =>
    request(`${API_BASE_URL}${API_ENDPOINTS.MATCHES.DELETE_COMMENT(commentId)}`, {
      method: 'DELETE',
    }),

  likeComment: (commentId) =>
    request(`${API_BASE_URL}${API_ENDPOINTS.MATCHES.LIKE_COMMENT(commentId)}`, {
      method: 'POST',
    }),
};

// Players API
export const playersAPI = {
  list: () => request(`${API_BASE_URL}${API_ENDPOINTS.PLAYERS.LIST}`),

  get: (id) => request(`${API_BASE_URL}${API_ENDPOINTS.PLAYERS.GET(id)}`),

  getRatings: (id) => request(`${API_BASE_URL}${API_ENDPOINTS.PLAYERS.RATINGS(id)}`),

  rate: (playerId, score, comment) =>
    request(`${API_BASE_URL}${API_ENDPOINTS.PLAYERS.RATE(playerId)}`, {
      method: 'POST',
      body: JSON.stringify({ score, comment }),
    }),
};

// Users API
export const usersAPI = {
  getProfile: (id) => request(`${API_BASE_URL}${API_ENDPOINTS.USERS.PROFILE(id)}`),
  me: () => request(`${API_BASE_URL}${API_ENDPOINTS.USERS.ME}`),

  follow: (id) =>
    request(`${API_BASE_URL}${API_ENDPOINTS.USERS.FOLLOW(id)}`, {
      method: 'POST',
    }),

  unfollow: (id) =>
    request(`${API_BASE_URL}${API_ENDPOINTS.USERS.UNFOLLOW(id)}`, {
      method: 'DELETE',
    }),
};

// Teams API
export const teamsAPI = {
  list: () => request(`${API_BASE_URL}${API_ENDPOINTS.TEAMS.LIST}`),
  get: (id) => request(`${API_BASE_URL}${API_ENDPOINTS.TEAMS.GET(id)}`),
};

// Feed API
export const feedAPI = {
  get: () => request(`${API_BASE_URL}${API_ENDPOINTS.FEED.GET}`),
};

// Home API
export const homeAPI = {
  get: () => request(`${API_BASE_URL}/home`),
};

// Admin API
export const adminAPI = {
  createMatch: (data) =>
    request(`${API_BASE_URL}${API_ENDPOINTS.ADMIN.MATCHES}`, {
      method: 'POST',
      body: JSON.stringify(data),
    }),

  updateMatch: (id, data) =>
    request(`${API_BASE_URL}${API_ENDPOINTS.ADMIN.MATCH(id)}`, {
      method: 'PUT',
      body: JSON.stringify(data),
    }),

  createTeam: (data) =>
    request(`${API_BASE_URL}${API_ENDPOINTS.ADMIN.TEAMS}`, {
      method: 'POST',
      body: JSON.stringify(data),
    }),

  updateTeam: (id, data) =>
    request(`${API_BASE_URL}${API_ENDPOINTS.ADMIN.TEAM(id)}`, {
      method: 'PUT',
      body: JSON.stringify(data),
    }),

  createPlayer: (data) =>
    request(`${API_BASE_URL}${API_ENDPOINTS.ADMIN.PLAYERS}`, {
      method: 'POST',
      body: JSON.stringify(data),
    }),

  updatePlayer: (id, data) =>
    request(`${API_BASE_URL}${API_ENDPOINTS.ADMIN.PLAYER(id)}`, {
      method: 'PUT',
      body: JSON.stringify(data),
    }),
};
