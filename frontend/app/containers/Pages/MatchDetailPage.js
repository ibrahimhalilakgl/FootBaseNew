import React, { useEffect, useState } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import {
  Box,
  Card,
  CardContent,
  CircularProgress,
  Typography,
  Alert,
  Divider,
  Stack,
  TextField,
  Button,
  IconButton,
  Tooltip,
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import { matchesAPI } from 'utils/api';

function MatchDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [commentText, setCommentText] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [editingId, setEditingId] = useState(null);

  const load = async () => {
    try {
      setLoading(true);
      const res = await matchesAPI.get(id);
      setData(res);
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
    if (!commentText.trim()) return;
    try {
      setSubmitting(true);
      if (editingId) {
        await matchesAPI.updateComment(editingId, commentText.trim());
      } else {
        await matchesAPI.addComment(id, commentText.trim());
      }
      setCommentText('');
      setEditingId(null);
      await load();
    } catch (e) {
      setError('Yorum kaydedilemedi. Lütfen tekrar deneyin.');
      if (e?.response?.status === 401) {
        navigate('/login');
      }
    } finally {
      setSubmitting(false);
    }
  };

  const handleEdit = (comment) => {
    setEditingId(comment.id);
    setCommentText(comment.message);
  };

  const handleDelete = async (commentId) => {
    try {
      await matchesAPI.deleteComment(commentId);
      await load();
    } catch (e) {
      setError('Yorum silinemedi.');
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
        Maç Detayı
      </Typography>
      <Card variant="outlined">
        <CardContent>
          <Stack spacing={1}>
            <Typography variant="h6">
              {data.homeTeam || '-'} vs {data.awayTeam || '-'}
            </Typography>
            <Typography color="text.secondary">Durum: {data.status || '-'}</Typography>
            <Typography color="text.secondary">
              Başlama: {data.kickoffAt ? new Date(data.kickoffAt).toLocaleString('tr-TR') : '-'}
            </Typography>
            <Typography color="text.secondary">Stadyum: {data.venue || '-'}</Typography>
            <Divider />
            <Typography variant="subtitle1" fontWeight="bold">Yorumlar</Typography>
            {(data.comments || []).map((c) => (
              <Box key={c.id} pb={1}>
                <Stack direction="row" alignItems="center" justifyContent="space-between">
                  <Box>
                    <Typography variant="body1" fontWeight="bold">{c.author}</Typography>
                    <Typography variant="body2" color="text.secondary">
                      {c.message}
                    </Typography>
                    <Typography variant="caption" color="text.secondary">
                      {c.createdAt ? new Date(c.createdAt).toLocaleString('tr-TR') : ''}
                    </Typography>
                  </Box>
                  {c.canEdit && (
                    <Stack direction="row" spacing={1}>
                      <Tooltip title="Düzenle">
                        <IconButton size="small" onClick={() => handleEdit(c)}>
                          <EditIcon fontSize="small" />
                        </IconButton>
                      </Tooltip>
                      <Tooltip title="Sil">
                        <IconButton size="small" onClick={() => handleDelete(c.id)}>
                          <DeleteIcon fontSize="small" />
                        </IconButton>
                      </Tooltip>
                    </Stack>
                  )}
                </Stack>
              </Box>
            ))}
            {(data.comments || []).length === 0 && <Typography color="text.secondary">Henüz yorum yok.</Typography>}
            <Divider />
            <Typography variant="subtitle1" fontWeight="bold">Yorum ekle</Typography>
            <TextField
              multiline
              minRows={2}
              fullWidth
              value={commentText}
              onChange={(e) => setCommentText(e.target.value)}
              placeholder="Yorumunuzu yazın"
            />
            <Box display="flex" justifyContent="flex-end" gap={1}>
              {editingId && (
                <Button
                  variant="text"
                  onClick={() => { setEditingId(null); setCommentText(''); }}
                  disabled={submitting}
                >
                  İptal
                </Button>
              )}
              <Button
                variant="contained"
                onClick={handleSubmit}
                disabled={submitting || !commentText.trim()}
              >
                {editingId ? 'Güncelle' : 'Gönder'}
              </Button>
            </Box>
          </Stack>
        </CardContent>
      </Card>
      <Box mt={2}>
        <Link to="/app/matches">← Maç listesine dön</Link>
      </Box>
    </Box>
  );
}

export default MatchDetailPage;
