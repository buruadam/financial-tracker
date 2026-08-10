import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { Lock, Mail, User, ArrowRight, AlertCircle } from 'lucide-react';
import { useAuth } from '../../context/useAuth';

export default function RegisterPage() {
    const { register } = useAuth();
    const navigate = useNavigate();

    const [loading, setLoading] = useState(false);
    const [globalError, setGlobalError] = useState<string | null>(null);
    const [fieldErrors, setFieldErrors] = useState<Record<string, string[]>>({});

    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = async (e: React.SyntheticEvent<HTMLFormElement>) => {
        e.preventDefault();
        setLoading(true);
        setGlobalError(null);
        setFieldErrors({});

        try {
            await register({ username, email, password });
            navigate('/login', { state: { successMessage: 'Account created successfully! You can now log in.' } });
        } catch (err: any) {
            const errorData = err.response?.data;

            if (errorData) {
                if (errorData.invalid_fields && typeof errorData.invalid_fields === 'object') {
                    setFieldErrors(errorData.invalid_fields);
                }

                if (errorData.detail) {
                    setGlobalError(errorData.detail);
                } else if (errorData.message) {
                    setGlobalError(errorData.message);
                } else {
                    setGlobalError(errorData.title || 'An unexpected error occurred.');
                }
            } else {
                setGlobalError('Cannot connect to the server. Please check your connection.');
            }
        } finally {
            setLoading(false);
        }
    };

    return (
        <div>
            <div className="text-center mb-6">
                <h2 className="text-2xl font-bold text-zinc-950 tracking-tight">Create an account</h2>
                <p className="text-sm text-zinc-500 mt-1">Start tracking your expenses today</p>
            </div>

            {globalError && (
                <div className="mb-4 p-3 bg-red-50 border border-red-200 text-red-600 rounded-lg text-sm flex items-center gap-2">
                    <AlertCircle size={18} className="shrink-0" />
                    <span>{globalError}</span>
                </div>
            )}

            <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                    <label className="block text-sm font-medium text-zinc-700 mb-1">Username</label>
                    <div className="relative">
                        <User className="absolute left-3 top-1/2 -translate-y-1/2 text-zinc-400" size={18} />
                        <input
                            type="text"
                            required
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            autoComplete="username"
                            className={`w-full pl-10 pr-4 py-2 bg-zinc-50 border rounded-lg focus:outline-none focus:ring-2 text-zinc-900 transition-all ${fieldErrors.username ? 'border-red-300 focus:ring-red-500/20 focus:border-red-500' : 'border-zinc-200 focus:ring-emerald-500/20 focus:border-emerald-500'
                                }`}
                        />
                    </div>
                    {fieldErrors.username?.map((msg, i) => (
                        <p key={i} className="text-xs text-red-500 mt-1">• {msg}</p>
                    ))}
                </div>

                <div>
                    <label className="block text-sm font-medium text-zinc-700 mb-1">Email address</label>
                    <div className="relative">
                        <Mail className="absolute left-3 top-1/2 -translate-y-1/2 text-zinc-400" size={18} />
                        <input
                            type="email"
                            required
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            autoComplete="email"
                            className={`w-full pl-10 pr-4 py-2 bg-zinc-50 border rounded-lg focus:outline-none focus:ring-2 text-zinc-900 transition-all ${fieldErrors.email ? 'border-red-300 focus:ring-red-500/20 focus:border-red-500' : 'border-zinc-200 focus:ring-emerald-500/20 focus:border-emerald-500'
                                }`}
                        />
                    </div>
                    {fieldErrors.email?.map((msg, i) => (
                        <p key={i} className="text-xs text-red-500 mt-1">• {msg}</p>
                    ))}
                </div>

                <div>
                    <label className="block text-sm font-medium text-zinc-700 mb-1">Password</label>
                    <div className="relative">
                        <Lock className="absolute left-3 top-1/2 -translate-y-1/2 text-zinc-400" size={18} />
                        <input
                            type="password"
                            required
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            autoComplete="new-password"
                            className={`w-full pl-10 pr-4 py-2 bg-zinc-50 border rounded-lg focus:outline-none focus:ring-2 text-zinc-900 transition-all ${fieldErrors.password ? 'border-red-300 focus:ring-red-500/20 focus:border-red-500' : 'border-zinc-200 focus:ring-emerald-500/20 focus:border-emerald-500'
                                }`}
                        />
                    </div>
                    {fieldErrors.password?.map((msg, i) => (
                        <p key={i} className="text-xs text-red-500 mt-1">• {msg}</p>
                    ))}
                </div>

                <button
                    type="submit"
                    disabled={loading}
                    className="w-full mt-2 bg-emerald-600 hover:bg-emerald-700 text-white font-medium py-2 px-4 rounded-lg transition-colors flex items-center justify-center gap-2 disabled:opacity-50 disabled:cursor-not-allowed cursor-pointer"
                >
                    {loading ? (
                        <div className="h-5 w-5 animate-spin rounded-full border-2 border-white border-t-transparent" />
                    ) : (
                        <>
                            <span>Register</span>
                            <ArrowRight size={18} />
                        </>
                    )}
                </button>
            </form>

            <div className="mt-6 text-center text-sm text-zinc-600">
                Already have an account?{' '}
                <Link to="/login" className="text-emerald-600 hover:text-emerald-700 font-semibold transition-colors">
                    Log in
                </Link>
            </div>
        </div>
    );
}
