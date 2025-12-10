import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import {
  Box,
  Card,
  CardContent,
  CircularProgress,
  Typography,
  Alert,
  Stack,
  Divider,
  TextField,
  Button,
  Rating,
} from '@mui/material';
import { playersAPI } from 'utils/api';

function PlayerDetailPage() {
  const { id } = useParams();
  const [data, setData] = useState(null);
  const [ratings, setRatings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [score, setScore] = useState(7);
  const [comment, setComment] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const load = async () => {
    try {
      setLoading(true);
      const [playerRes, ratingRes] = await Promise.all([
        playersAPI.get(id),
        playersAPI.getRatings(id),
      ]);
      setData(playerRes);
      setRatings(ratingRes || []);
      setError(null);
    } catch (e) {
      setError('Veri yüklenemedi. Lütfen tekrar deneyin.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    let mounted = true;
    (async () => {
      if (!mounted) return;
      await load();
    })();
    return () => {
      mounted = false;
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [id]);

  const handleSubmit = async () => {
    try {
      setSubmitting(true);
      await playersAPI.rate(id, score, comment.trim());
      setComment('');
      await load();
    } catch (e) {
      setError('Değerlendirme eklenemedi. Lütfen tekrar deneyin.');
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" mt={4}>
        <CircularProgress />
      </Box>
    );
  }

  if (error || !data) {
    return (
      <Box mt={2}>
        <Alert severity="error">{error || 'Kayıt bulunamadı'}</Alert>
      </Box>
    );
  }

  return (
    <Box mt={2}>
      <Typography variant="h5" gutterBottom>
        Oyuncu Detayı
      </Typography>
      <Card variant="outlined">
        <CardContent>
          <Stack spacing={1}>
            <Typography variant="h6">{data.fullName}</Typography>
            <Typography color="text.secondary">Pozisyon: {data.position || '-'}</Typography>
            <Typography color="text.secondary">Forma: {data.shirtNumber || '-'}</Typography>
            <Typography color="text.secondary">Takım: {data.team || '-'}</Typography>
            <Typography color="text.secondary">Ortalama Puan: {data.averageRating ?? '-'}</Typography>
            <Typography color="text.secondary">Puanlama Sayısı: {data.ratingCount ?? 0}</Typography>
            <Divider />
            <Typography variant="subtitle1">Değerlendirmeler</Typography>
            {(ratings || []).map((r) => (
              <Box key={r.id} pb={1}>
                <Stack direction="row" alignItems="center" spacing={1}>
                  <Rating size="small" value={Number(r.score) || 0} readOnly max={10} />
                  <Typography variant="body2" color="text.secondary">
                    {r.author || '-'}
                  </Typography>
                </Stack>
                {r.comment && (
                  <Typography variant="body2" color="text.primary">
                    {r.comment}
                  </Typography>
                )}
              </Box>
            ))}
            {(ratings || []).length === 0 && (
              <Typography color="text.secondary">Değerlendirme yok</Typography>
            )}
            <Divider />
            <Typography variant="subtitle1">Değerlendirme ekle</Typography>
            <Stack direction="row" alignItems="center" spacing={1}>
              <Rating
                value={score}
                max={10}
                onChange={(_, val) => setScore(val || 1)}
              />
              <Typography>{score}</Typography>
            </Stack>
            <TextField
              multiline
              minRows={2}
              fullWidth
              value={comment}
              onChange={(e) => setComment(e.target.value)}
              placeholder="Yorum (opsiyonel)"
            />
            <Box display="flex" justifyContent="flex-end">
              <Button
                variant="contained"
                onClick={handleSubmit}
                disabled={submitting}
              >
                Gönder
              </Button>
            </Box>
          </Stack>
        </CardContent>
      </Card>
      <Box mt={2}>
        <Link to="/app/players">← Oyuncu listesine dön</Link>
      </Box>
    </Box>
  );
}

export default PlayerDetailPage;

