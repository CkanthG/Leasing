import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {

  const token = localStorage.getItem('accessToken');

  if (token) {
    req = req.clone({

      setHeaders: {
        Authentication: `Bearer ${token}`
      }
    });
  }

  return next(req);
};
