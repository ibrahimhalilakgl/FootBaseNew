import React, { useEffect, useState } from 'react';
import Grid from '@mui/material/Grid';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';
import Divider from '@mui/material/Divider';
import CircularProgress from '@mui/material/CircularProgress';
import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
import Stack from '@mui/material/Stack';
import Avatar from '@mui/material/Avatar';
import Chip from '@mui/material/Chip';
import { homeAPI } from 'utils/api';

function HomePage() {
  const [upcomingMatches, setUpcomingMatches] = useState([]);
  const [comments, setComments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    let mounted = true;
    async function load() {
      try {
        const data = await homeAPI.get();
        if (!mounted) return;
        setUpcomingMatches(data.upcomingMatches || data.matches || []);
        setComments(data.comments || []);
      } catch (err) {
        if (mounted) setError('Veri yüklenemedi. Lütfen tekrar deneyin.');
      } finally {
        if (mounted) setLoading(false);
      }
    }
    load();
    return () => {
      mounted = false;
    };
  }, []);

  const section = (title, content) => (
    <Card variant="outlined" sx={{ height: '100%' }}>
      <CardContent>
        <Typography variant="h6" gutterBottom>
          {title}
        </Typography>
        {content}
      </CardContent>
    </Card>
  );

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" mt={4}>
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Box mt={2}>
        <Alert severity="error">{error}</Alert>
      </Box>
    );
  }

  return (
    <Box mt={2}>
      <Typography variant="h5" gutterBottom>
        Ana Sayfa
      </Typography>
      <Grid container spacing={2}>
        <Grid item xs={12} md={6}>
          {section(
            'Son Yorumlar',
            <List dense>
              {comments.map((c, idx) => (
                <React.Fragment key={`${c.id || idx}`}>
                  <ListItem alignItems="flex-start">
                    <ListItemText
                      primary={c.message || 'Yeni yorum'}
                      secondary={[
                        c.author,
                        c.createdAt ? new Date(c.createdAt).toLocaleString('tr-TR') : null,
                      ]
                        .filter(Boolean)
                        .join(' • ')}
                    />
                  </ListItem>
                  <Divider component="li" />
                </React.Fragment>
              ))}
              {comments.length === 0 && (
                <ListItem>
                  <ListItemText primary="Henüz yorum yok." />
                </ListItem>
              )}
            </List>,
          )}
        </Grid>
        <Grid item xs={12} md={6}>
          {section(
            'Yaklaşan Maçlar',
            <Stack spacing={2}>
              {upcomingMatches.map((m) => (
                <Card key={m.id} variant="outlined">
                  <CardContent>
                    <Stack spacing={1.5}>
                      <Stack direction="row" spacing={2} alignItems="center" justifyContent="space-between">
                        <Stack direction="row" spacing={1.5} alignItems="center">
                          <Avatar src={m.homeTeamLogo || undefined} alt={m.homeTeam || 'Ev sahibi'} />
                          <Typography variant="subtitle1" fontWeight="bold">{m.homeTeam || '-'}</Typography>
                        </Stack>
                        <Typography variant="h6" fontWeight="bold">
                          {m.homeScore != null ? m.homeScore : '-'} - {m.awayScore != null ? m.awayScore : '-'}
                        </Typography>
                        <Stack direction="row" spacing={1.5} alignItems="center">
                          <Typography variant="subtitle1" fontWeight="bold">{m.awayTeam || '-'}</Typography>
                          <Avatar src={m.awayTeamLogo || undefined} alt={m.awayTeam || 'Deplasman'} />
                        </Stack>
                      </Stack>
                      <Stack direction="row" spacing={1} alignItems="center" justifyContent="space-between">
                        <Typography color="text.secondary">
                          {m.kickoffAt ? new Date(m.kickoffAt).toLocaleString('tr-TR') : 'Tarih bekleniyor'}
                        </Typography>
                        <Chip label={m.status || 'PLANLI'} size="small" />
                      </Stack>
                    </Stack>
                  </CardContent>
                </Card>
              ))}
              {upcomingMatches.length === 0 && (
                <Typography color="text.secondary">Planlanan maç bulunamadı.</Typography>
              )}
            </Stack>,
          )}
        </Grid>
      </Grid>
    </Box>
  );
}

export default HomePage;
