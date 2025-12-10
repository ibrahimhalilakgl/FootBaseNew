export const RESTART_ON_REMOUNT = '@@saga-injector/restart-on-remount';
export const DAEMON = '@@saga-injector/daemon';
export const ONCE_TILL_UNMOUNT = '@@saga-injector/once-till-unmount';

// API Configuration
export const API_BASE_URL = process.env.API_URL || 'http://localhost:8080/api';
export const API_ENDPOINTS = {
  AUTH: {
    LOGIN: '/auth/login',
    REGISTER: '/auth/register',
    RESET_PASSWORD: '/auth/reset-password',
  },
  MATCHES: {
    LIST: '/matches',
    GET: (id) => `/matches/${id}`,
    PREDICT: (id) => `/matches/${id}/predictions`,
    COMMENT: (id) => `/matches/${id}/comments`,
    DELETE_COMMENT: (id) => `/matches/comments/${id}`,
    LIKE_COMMENT: (id) => `/matches/comments/${id}/like`,
    SEARCH: '/matches',
  },
  PLAYERS: {
    LIST: '/players',
    GET: (id) => `/players/${id}`,
    RATINGS: (id) => `/players/${id}/ratings`,
    RATE: (id) => `/players/${id}/ratings`,
  },
  TEAMS: {
    LIST: '/teams',
    GET: (id) => `/teams/${id}`,
  },
  USERS: {
    PROFILE: (id) => `/users/${id}`,
    ME: '/users/me',
    FOLLOW: (id) => `/users/${id}/follow`,
    UNFOLLOW: (id) => `/users/${id}/follow`,
  },
  FEED: {
    GET: '/feed',
  },
  ADMIN: {
    MATCHES: '/admin/matches',
    MATCH: (id) => `/admin/matches/${id}`,
    TEAMS: '/admin/teams',
    TEAM: (id) => `/admin/teams/${id}`,
    PLAYERS: '/admin/players',
    PLAYER: (id) => `/admin/players/${id}`,
  },
};
