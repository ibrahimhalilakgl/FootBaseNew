const isAuthed = () => {
  if (typeof window === 'undefined') return false;
  return Boolean(localStorage.getItem('authToken'));
};

const baseMenu = [
  {
    key: 'home',
    name: 'Ana Sayfa',
    link: '/app',
    icon: 'ion-ios-home'
  },
  {
    key: 'matches',
    name: 'Maçlar',
    link: '/app/matches',
    icon: 'ion-ios-football'
  },
  {
    key: 'players',
    name: 'Oyuncular',
    link: '/app/players',
    icon: 'ion-ios-people'
  },
  {
    key: 'teams',
    name: 'Takımlar',
    link: '/app/teams',
    icon: 'ion-ios-people'
  }
];

const authMenu = [
  {
    key: 'auth',
    name: 'Giriş / Kayıt',
    link: '/login',
    icon: 'ion-log-in'
  }
];

const profileMenu = [
  {
    key: 'profile',
    name: 'Profilim',
    link: '/app/profile',
    icon: 'ion-ios-person'
  }
];

const menu = [
  {
    key: 'footbase',
    name: 'FootBase',
    icon: 'ion-ios-football',
    child: isAuthed() ? [...baseMenu, ...profileMenu] : [...authMenu, ...baseMenu],
  }
];

module.exports = menu;
