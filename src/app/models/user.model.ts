export interface User {
  id: number;
  firstName: string;
  lastName: string;
  maidenName?: string;
  age: number;
  gender: string;
  email: string;
  phone: string;
  username: string;
  birthDate?: string;
  image: string;
  university?: string;
  role?: string;
  address?: {
    address: string;
    city: string;
    state: string;
    country?: string;
  };
  company?: {
    department: string;
    name: string;
    title: string;
  };
}
